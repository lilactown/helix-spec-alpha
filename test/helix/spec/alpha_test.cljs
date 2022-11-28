(ns helix.spec.alpha-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.spec.alpha :as s]
   [helix.spec.alpha :as helix.spec]))

(s/def :foo/bar string?)
(s/def ::props (helix.spec/props (s/keys :req [:foo/bar])))

(deftest valid?
  (is (s/valid? ::props #js {"foo/bar" "baz"}))
  (is (not (s/valid? ::props #js {"foo/bar" 1}))))

(deftest conform
  (is (= (s/conform ::props #js {"foo/bar" "baz"})
         #:foo{:bar "baz"}))
  (is (= (s/conform ::props #js {"foo/bar" 1})
         :cljs.spec.alpha/invalid)))

(deftest explain
  (is (= (s/explain-str ::props #js {"foo/bar" "baz"})
         "Success!\n"))
  (let [o #js {"foo/bar" 1}]
    (is (= `{:cljs.spec.alpha/problems
             ({:path [:foo/bar]
               :pred cljs.core/string?
               :val 1
               :via [:helix.spec.alpha-test/props :foo/bar]
               :in [:foo/bar]})
             :cljs.spec.alpha/spec :helix.spec.alpha-test/props
             :cljs.spec.alpha/value ~o}
           (s/explain-data ::props o)))))

(comment
  (clojure.test/run-tests))
