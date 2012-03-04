(ns evaljs.rhino
  "Functions to create a Javascript context using the Rhino library."
  (:use evaljs.core)
  (:import [clojure.lang ISeq Keyword]
           [java.util List Map]
           [org.mozilla.javascript
            Context
            NativeArray
            NativeObject]))

(defprotocol AsClojure
  (as-clojure [x]))

(extend-protocol AsClojure
  NativeObject
  (as-clojure [x] (into {} (for [[k v] x] [k (as-clojure v)])))
  NativeArray
  (as-clojure [x] (vec (map as-clojure x)))
  Object
  (as-clojure [x] x))

(defprotocol AsJavascript
  (as-javascript [x]))

(defn- populate-map [m coll]
  (doseq [[k v] coll]
    (.put m (name k) m v)))

(defn- native-object [coll]
  (doto (NativeObject.) (populate-map coll)))

(defn- native-array [coll]
  (NativeArray. (into-array Object coll)))

(extend-protocol AsJavascript
  NativeObject
  (as-javascript [x] x)
  NativeArray
  (as-javascript [x] x)
  Keyword
  (as-javascript [x] (name x))
  ISeq
  (as-javascript [x] (native-array (map as-javascript x)))
  List
  (as-javascript [x] (native-array (map as-javascript x)))
  Map
  (as-javascript [x] (native-object (for [[k v] x] [k (as-javascript v)])))
  Object
  (as-javascript [x] x))

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
         (.put scope (name k) scope (as-javascript v)))
       (RhinoContext. context scope))))