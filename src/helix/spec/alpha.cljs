(ns helix.spec.alpha
  (:require
   [cljs-bean.core :as b]
   [clojure.spec.alpha :as s]
   [helix.core :as helix]
   [helix.impl.props]))


(defn props
  [spec]
  (reify
    s/Spec
    (conform* [_ x]
      (try
        (let [m (into {} (helix/extract-cljs-props x))]
          (s/conform* spec m))
        (catch js/Object _e
          ::invalid)))
    (unform* [_ m]
      (let [x (s/unform* spec m)]
        (helix.impl.props/-props x)))
    (explain* [_ path via in x]
      (if-not (object? x)
        [{:path path :pred `object? :val x :via via :in in}]
        (s/explain* spec path via in (into {} (helix/extract-cljs-props x)))))
    (gen* [_ overrides path rmap]
      (throw (ex-info "Not implemented" {:path path :rmap rmap})))
    (with-gen* [_ gfn]
      (throw (ex-info "Not implemented" {})))
    (describe* [_]
      [`props spec])))
