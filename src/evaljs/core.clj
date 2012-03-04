(ns evaljs.core
  "Functions for evaluating Javascript in Clojure. Needs a Javascript context
  in order to work. See: evaljs.rhino.")

(defprotocol JSContext
  (evaljs* [context code])
  (destroy [context]))

(declare ^:dynamic *context*)

(defn evaljs
  "Evaluate a string of Javascript in the current context."
  [code]
  (evaljs* *context* code))

(defmacro with-context
  "Evaluates the body with the supplied context, making sure to destroy the
  context when the form ends."
  [context & body]
  `(let [context# ~context]
     (try
       (binding [*context* context#] ~@body)
       (finally (destroy context#)))))