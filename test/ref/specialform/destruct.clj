(ns ref.specialform.destruct
  (:use clojure.test))

(deftest map-tests
  (testing "Entire map"
    (let [entiremap {:a 1 :b 2}]
      (is (= entiremap {:a 1 :b 2}))))

  (testing "Single key"
    (let [structure {:a 1 :b 2}
          {val :a} structure]
      (is (= val 1))))

  (testing "Single key and entire map"
    (let [structure {:a 1 :b 2}
          {val :a :as entiremap} structure]
      (is (= val 1))
      (is (= entiremap {:a 1 :b 2}))))

  (testing "Single key in :keys vec"
    (let [structure {:a 1 :b 2}
          {:keys [a]} structure]
      (is (= a 1))))

  (testing "Single key in nested map"
    (let [structure {:a 1 :b { :c 3 }}
           {{val :c} :b} structure]
      (is (= val 3))))

  (testing "Single key in nested map, with key provided as an arg"
    (let [keyarg :c
          structure {:a 1 :b { :c 3 :d 4 }}
          {{val keyarg} :b} structure]
      (is (= val 3))))

  (testing "Vector in map"
    (let [structure {:a 1 :b [ 2 3 4 5 ]}
          {[ _ _ val] :b} structure]
      (is (= val 4))))

  (testing "Map in vector"
    (let [structure [ 2 3 4 { :a 5 :b 6}]
          [ _ _ _ { val :b }] structure]
      (is (= val 6))))

  )
