(ns helix.spec.alpha
  (:require
   [cljs-bean.core :as b]
   [clojure.spec.alpha :as s]
   [helix.core :as helix]
   [helix.impl.props])
  (:require-macros
   [helix.spec.alpha]))


(prn (into {} (b/bean #js {:a 1 :b 2})))

(defn prop-spec-impl
  [spec]
  (reify
    s/Spec
    (conform* [_ x]
      (try
        (let [m (into {} (helix/extract-cljs-props x))]
          (s/conform* spec m))
        (catch js/Object _e
          (prn _e)
          ::invalid)))
    (unform* [_ m]
      (let [x (s/unform* spec m)]
        (helix.impl.props/-props x)))
    (explain* [_ path via in x]
      (if-not (object? x)
        [{:path path :pred `object? :val x :via via :in in}]
        (s/explain* spec path via in x)))
    (gen* [_ overrides path rmap]
      (throw (ex-info "Not implemented" {:path path :rmap rmap})))
    (with-gen* [_ gfn]
      (throw (ex-info "Not implemented" {})))
    (describe* [_]
      [`props spec])))
