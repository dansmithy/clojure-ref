(ns ref.playground.robots)

(defn anticlockwise [[x y]]
	(if (= x 0) [(* -1 y) x] [y x]))

(defn clockwise [[x y]]
	(if (= x 0) [y x] [y (* -1 x)]))

(defn turn-anticlockwise [robot world] 
  (update-in robot [:velocity] anticlockwise))

(defn turn-clockwise [robot world] 
  (update-in robot [:velocity] clockwise))

(defn ahead-position [{:keys [position velocity]}]
  (map + position velocity))

(defn valid-location? [{:keys [height width blocks]} [x y]]
  (and (<= 0 x (dec width))
       (<= 0 y (dec height))
       (not (some #{[x y]} blocks))))

(defn pickup-tokens [
                      {position :position robot-tokens :tokens :as robot}
                      {:keys [tokens]}]
  (if (some #{position} tokens)
    (assoc robot :tokens (conj robot-tokens position))
    robot))

(defn move-forward [robot world] 
  (let [position (ahead-position robot)]
       (if (valid-location? world position)
         (-> robot
             (assoc :position position)
             (pickup-tokens world))
         robot)))

(defn run [robot world program]
  (if-let [task (first program)]
     (run
       (if (coll? task)
	       (run robot world task)
         (task robot world))
        world
       (rest program))
     robot))

(def function1 [move-forward
                move-forward
                move-forward])

(def function2 [turn-clockwise
                function1])

(def program [
              function1
              turn-anticlockwise
              function1
              turn-anticlockwise
              turn-anticlockwise
              move-forward
              function2
              function2
              function2])

(def robot { :position [0 2]
             :velocity [0 -1]
             :tokens #{} })

(def world { :width 5
             :height 3
             :tokens #{[2 0] [4 0]}
             :blocks #{[2 1] [3 0]}})

(def a (run robot world program))

(def message (let [toks (count (:tokens a))]
   (if (= 2 toks) 
     "You did it!" 
     (str "Not yet, you got " toks " tokens"))))