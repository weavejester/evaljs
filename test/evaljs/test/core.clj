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

(deftest import-vars
  (with-context (rhino-context {:x 1, :s "foo"})
    (is (= (evaljs "x") 1))
    (is (= (evaljs "s") "foo")))
  (with-context (rhino-context {:x [1 2]})
    (is (= (evaljs "x") [1 2]))
    (is (= (evaljs "x[0]") 1)))
  (with-context (rhino-context {:x {:a 3}})
    (is (= (evaljs "x") {"a" 3}))
    (is (= (evaljs "x.a") 3))))
