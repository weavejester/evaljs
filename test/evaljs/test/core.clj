(ns evaljs.test.core
  (:use evaljs.core
        evaljs.rhino
        clojure.test))

(deftest basic-js
  (with-context (rhino-context)
    (is (= (evaljs "1 + 1") 2))))
