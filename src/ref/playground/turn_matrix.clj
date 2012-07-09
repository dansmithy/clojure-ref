(ns ref.playground.turn-matrix)

(def clockwise-matrix [[0 1][-1 0]])

(def velocities [[0 1] [1 0] [0 -1] [-1 0]])

(defn matrix-multiply [velocity turn-matrix]
  (->>
    turn-matrix
    (map (partial map * velocity))
    (map (partial reduce +))))

(defn turn-clockwise [velocity] (matrix-multiply velocity clockwise-matrix))

(def clockwise-results
  (map turn-clockwise velocities))

(=
  clockwise-results
  '([1 0] [0 -1] [-1 0] [0 1]))


