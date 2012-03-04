(ns evaljs.rhino
  "Functions to create a Javascript context using the Rhino library."
  (:use evaljs.core)
  (:import [org.mozilla.javascript
            Context
            NativeArray
            NativeObject]))

(defprotocol AsClojure
  (as-clojure [x]))

(extend-protocol AsClojure
  NativeObject
  (as-clojure [x] (into {} x))
  NativeArray
  (as-clojure [x] (vec x))
  Object
  (as-clojure [x] x))

(deftype RhinoContext [context scope]
  JSContext
  (evaljs* [_ code]
    (as-clojure (.evaluateString context scope code "<eval>" 1 nil)))
  (destroy [_]
    (Context/exit)))

(defn rhino-context
  "Create a JSContext using the Rhino Javascript interpreter. Takes an optional
  map of variables to import into the environment."
  ([] (rhino-context {}))
  ([vars]
     (let [context (Context/enter)
           scope   (.initStandardObjects context)]
       (doseq [[k v] vars]
         (.put scope (name k) scope v))
       (RhinoContext. context scope))))