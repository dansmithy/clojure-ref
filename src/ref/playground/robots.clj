(ns ref.playground.robots)

(defn turn-left [[x y]]
	(if (= x 0) [(* -1 y) x] [y x]))

(defn turn-right [[x y]]
	(if (= x 0) [y x] [y (* -1 x)]))

(defn left [robot world] 
  (update-in robot [:direction] turn-left))

(defn right [robot world] 
  (update-in robot [:direction] turn-right))

(defn ahead-pos [{:keys [position direction]}]
  (map + position direction))

(defn valid-loc? [{:keys [height width blocks]} [x y]]
  (and (<= 0 x (dec width))
       (<= 0 y (dec height))
       (not (some #{[x y]} blocks))))

(defn pickup-tokens [{position :position robot-tokens :tokens :as robot} {:keys [tokens]}]
  (if (some #{position} tokens)
    (assoc robot :tokens (conj robot-tokens position))
    robot))

(defn forward [robot world] 
  (let [pos (ahead-pos robot)]
       (if (valid-loc? world pos)
         (-> robot
             (assoc :position pos)
             (pickup-tokens world))
         robot)))

(defn run [ robot world program ]
  (if-let [task (first program)]
     (run
       (if (coll? task)
	     (run robot world task)
         (task robot world))
         world
       (rest program))
     robot))



(def fn1 [forward forward])

(def program [ 
              forward 
              fn1
              left 
              forward 
              forward
              left
              left
              forward
              right
              forward
              forward
              forward
              right
              forward
              forward
              forward
              right
              forward
              forward])

(def robot { :position [0 2] :direction [0 -1]
             :tokens #{} })

(def world { :height 3 :width 5 :tokens [[2 0] [4 0]] 
            :blocks [[2 1] [3 0]] })

(def a (run robot world program))

(def message (let [toks (count (:tokens a))]
   (if (= 2 toks) 
     "You did it!" 
     (str "Not yet, you got " toks " tokens"))))