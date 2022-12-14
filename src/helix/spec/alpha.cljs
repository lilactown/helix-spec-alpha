(ns helix.spec.alpha
  (:require
   [cljs-bean.core :as b]
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [helix.core :as helix]
   [helix.impl.props]))

(defn props
  ([spec] (props spec nil))
  ([spec gfn]
   (let [*spec (delay (#'s/specize spec))]
     (reify
       s/Specize
       (specize* [this] this)
       (specize* [this _] this)

       s/Spec
       (conform* [_ x]
         (try
           (let [m (into {} (helix/extract-cljs-props x))] ; ensure its a map
             (s/conform* @*spec m))
           (catch js/Object _e
             ::invalid)))
       (unform* [_ m]
         (let [x (s/unform* @*spec m)]
           (helix.impl.props/-props x)))
       (explain* [_ path via in x]
         (if-not (object? x)
           [{:path path :pred `object? :val x :via via :in in}]
           ;; TODO should catch and explain error while extracting
           (s/explain* @*spec path via in (into {} (helix/extract-cljs-props x)))))
       (gen* [_ overrides path rmap]
         (if gfn
           (gen/fmap helix.impl.props/-props (gfn))
           (gen/fmap helix.impl.props/-props (s/gen* @*spec overrides path rmap))))
       (with-gen* [_ gfn]
         (props spec gfn))
       (describe* [_]
         [`props @*spec])))))
