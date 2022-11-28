(ns user
  (:require
   ["react-dom/client" :as rdom]
   [cljs.pprint :refer [pprint]]
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [helix.core :refer [defnc defcomponent $]]
   [helix.dom :as d]
   [helix.spec.alpha :as h.s]))


(def root (rdom/createRoot (js/document.getElementById "app")))


(s/def ::foo string?)

(defnc my-comp
  [{:keys [::foo]}]
  (d/div (str foo "bar")))

(defcomponent spec-boundary
  (constructor
   [this]
   (set! (.-state this) #js {:error nil}))

  ^:static (getDerivedStateFromError
   [_ e]
   #js {:error e})

  (render
   [_ props state]
   (if-let [error (.-error state)]
     (d/pre (with-out-str (pprint error)))
     (:children props))))

(defnc app
  []
  ($ spec-boundary
     ;; bad props
     ($ my-comp {::foo 1})))


(s/fdef my-comp
  :args (s/cat :props (h.s/prop-spec-impl (s/keys :req [::foo]))
               :ref object?))

(stest/instrument `my-comp)

(.render root ($ app))
