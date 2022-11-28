(ns helix.spec.alpha-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   clojure.test.check
   clojure.test.check.properties
   [helix.spec.alpha :as helix.spec]))

(s/def :foo/bar string?)
(s/def ::props (helix.spec/props (s/keys :req [:foo/bar])))

(s/def ::map (s/keys :req [:foo/bar]))
(s/def ::props-1 (helix.spec/props ::map))

(deftest valid?
  (is (s/valid? ::props #js {"foo/bar" "baz"}))
  (is (not (s/valid? ::props #js {"foo/bar" 1})))
  (is (s/valid? ::props-1 #js {"foo/bar" "baz"}))
  (is (not (s/valid? ::props-1 #js {"foo/bar" 1}))))

(deftest conform
  (is (= #:foo{:bar "baz"}
         (s/conform ::props #js {"foo/bar" "baz"})))
  (is (= :cljs.spec.alpha/invalid
         (s/conform ::props #js {"foo/bar" 1})))
  (is (= #:foo{:bar "baz"}
         (s/conform ::props-1 #js {"foo/bar" "baz"})))
  (is (= :cljs.spec.alpha/invalid
         (s/conform ::props-1 #js {"foo/bar" 1}))))

(deftest explain
  (is (= "Success!\n" (s/explain-str ::props #js {"foo/bar" "baz"})))
  (let [o #js {"foo/bar" 1}]
    (is (= `{:cljs.spec.alpha/problems
             ({:path [:foo/bar]
               :pred cljs.core/string?
               :val 1
               :via [:helix.spec.alpha-test/props :foo/bar]
               :in [:foo/bar]})
             :cljs.spec.alpha/spec :helix.spec.alpha-test/props
             :cljs.spec.alpha/value ~o}
           (s/explain-data ::props o))))
  (is (= "Success!\n" (s/explain-str ::props-1 #js {"foo/bar" "baz"})))
  (let [o #js {"foo/bar" 1}]
    (is (= `{:cljs.spec.alpha/problems
             ({:path [:foo/bar]
               :pred cljs.core/string?
               :val 1
               :via [:helix.spec.alpha-test/props-1 :foo/bar]
               :in [:foo/bar]})
             :cljs.spec.alpha/spec :helix.spec.alpha-test/props-1
             :cljs.spec.alpha/value ~o}
           (s/explain-data ::props-1 o)))))

(defn foo
  [o]
  (if (object? o)
    (if-let [bar (aget o "foo/bar")]
      (or (string? bar) (throw (ex-info "Not a string" {})))
      true)
    (throw (ex-info "Not an object" {}))))

(s/fdef foo
  :args (s/cat :props ::props))

(deftest check
  (is (= {:total 1, :check-passed 1}
         (stest/summarize-results (stest/check `foo)))))

(comment
  (clojure.test/run-tests))
