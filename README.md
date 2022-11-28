# helix.spec.alpha

[clojure.spec.alpha](https://github.com/clojure/spec.alpha) support for React
components defined using [helix](https://github.com/lilactown/helix).

## Installation

Using git deps

```clojure
town.lilac/helix-spec {:git/url "https://github.com/lilactown/helix-spec-alpha.git"
                       :git/sha "b78f7eaf9fedeecb648f8c25a233d1b3cff88c2f"}
```

## Features

- [x] Define specs for props
- [x] Instrumenting components
- [x] Explain props
- [x] Conform props
- [x] Unform props
- [ ] Generators for props
- [ ] Error boundary component for catching and explaining component spec errors

## Usage

```clojure
(ns app.feature
  (:require
   [clojure.spec.alpha :as s]
   [helix.core :refer [defnc]]
   [helix.spec.alpha :as helix.spec]))


(defnc my-comp
  [{:keys [::foo]}]
  (d/div (str foo "bar")))

(s/fdef my-comp
  :args (s/cat :props (helix.spec/props (s/keys :req [::foo]))
               :ref object?))

(comment
  ;; instrument, which will cause an error to be thrown during render if props
  ;; do not pass the spec
  (require '[clojure.spec.test.alpha :as stest])
  (stest/instrument `my-comp))
```

Recommended usage is to combine this with an error boundary that will catch and
print the spec error in a human-readable way.

## Developing

The library is meant to be used with a browser REPL.

With Emacs, run `M-x cider-jack-in-cljs` and select `shadow-cljs` as the REPL
type. Once connected, select `browser-repl`.

Code in [dev/user.cljs](./dev/user.cljs) contains code to run to verify
functionality.

## License

Licensed under EPL 2.0. Copyright Will Acton.
