# helix.spec.alpha

[clojure.spec.alpha](https://github.com/clojure/spec.alpha) support for React
components defined using [helix](https://github.com/lilactown/helix).

## Installation

Using git deps

```clojure
town.lilac/helix-spec {:git/url "https://github.com/lilactown/helix-spec-alpha.git"
                       :git/sha "c7109fed1d3dc743cd7e338b6dbadaa9951270f7"}
```

## Features

- [x] Define specs for props
- [x] Instrumenting components
- [x] Explain props
- [x] Conform props
- [x] Unform props
- [x] Generators for props
- [ ] Error boundary component for catching and explaining component spec errors
- [ ] Guidance and support for reducing code size

## Usage

```clojure
(ns app.feature
  (:require
   [clojure.spec.alpha :as s]
   [helix.core :refer [defnc]]
   [helix.spec.alpha :as helix.spec]))


(defnc my-comp
  "Just a typical component"
  [{:keys [::foo ::bar]}]
  (d/div (str foo ", " bar)))


;; define our individual prop keys as normal specs
(s/def ::foo string?)
(s/def ::bar string?)


;; define our props map using regular specs like s/keys, s/merge, s/map-of, etc.
(s/def ::my-comp-props-map (s/keys :req [::foo ::bar]))

;; and pass it to the `helix.spec.alpha/props` spec
(s/def ::my-comp-props (helix.spec/props ::my-comp-props-map))

(s/fdef my-comp
  :args (s/cat :props ::my-comp-props
               :ref object?))



;; we can also do all of the above inline
(s/fdef my-comp
  :args (s/cat :props (helix.spec/props
                       (s/keys :req [::foo ::bar]))
               :ref object?))
```

### Instrumenting

Components can be instrumented just like any other function.

Recommended usage is to combine this with an error boundary that will catch and
print the spec error in a human-readable way.

```clojure
(comment
  ;; instrument, which will cause an error to be thrown during render if props
  ;; do not pass the spec
  (require '[clojure.spec.test.alpha :as stest])
  (stest/instrument `my-comp))
```

### Code size

> **Warning**

`helix.spec.alpha` gives no consideration to code size. Specs defined using
`clojure.spec.alpha` will add to your bundle whether you use the specs at
runtime or not in your production build.

There are other libraries, like [Guardrails](https://github.com/fulcrologic/guardrails)
which provide the ability to remove the specs at build time, but Guardrails has
its own custom syntax. I have not tried using it in practice.

## Developing

The library can be developed using a simple browser REPL.

With Emacs, run `M-x cider-jack-in-cljs` and select `shadow-cljs` as the REPL
type. Once connected, select `browser-repl`.

Code in [dev/user.cljs](./dev/user.cljs) contains code to run to verify
functionality.

Tests can be run from the command line using `clojure -M:test`.

## License

Licensed under EPL 2.0. Copyright Will Acton.
