# evaljs

Evaljs is a library for evaluating Javascript code in Clojure.

## Installation

Add the following dependency to your `project.clj` file:

    [evaljs "0.1.2"]

## Usage

You'll need both the `evaljs.core` and `evaljs.rhino` namespaces for
this to work:

```clojure
(use 'evaljs.core)
(use 'evaljs.rhino)
```

You can then evaluate a Javascript expression within a blank context:

```clojure
(with-context (rhino-context)
  (prn (evaljs "1 + 1")))
```

Or a context with a map of initial variables:

```clojure
(with-context (rhino-context {:x 2})
  (prn (evaljs "x + 1")))
```

The return value of `evaljs` is the last statement executed. Basic
types like maps and arrays are automaitcally converted between Clojure
and Javascript types.

If you want to import a Javascript library, you can evaluate an I/O
object such as a file or resource:

```clojure
(require '[clojure.java.io :as io])

(with-context (rhino-context)
  (evaljs (io/resource "some/js/library.js"))
  (evaljs "someJSLibrary()"))
```

## License

Copyright (C) 2012 James Reeves

Distributed under the Eclipse Public License, the same as Clojure.
