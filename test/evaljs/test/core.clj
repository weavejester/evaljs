(ns evaljs.test.core
  (:use evaljs.core
        evaljs.rhino
        clojure.test))

(deftest basic-types
  (with-context (rhino-context)
    (is (= (evaljs "10") 10))
    (is (= (evaljs "\"foo\"") "foo"))
    (is (= (evaljs "[1, 2]") [1 2]))
    (is (= (evaljs "x={a:2};x") {"a" 2}))))
