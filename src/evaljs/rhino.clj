(ns evaljs.rhino
  "Functions to create a Javascript context using the Rhino library."
  (:use evaljs.core)
  (:import [org.mozilla.javascript Context]))

(deftype RhinoContext [context scope]
  JSContext
  (evaljs* [_ code]
    (.evaluateString context scope code "<eval>" 1 nil))
  (destroy [_]
    (Context/exit)))

(defn rhino-context
  "Create a JSContext using the Rhino Javascript interpreter."
  []
  (let [context (Context/enter)
        scope   (.initStandardObjects context)]
    (RhinoContext. context scope)))