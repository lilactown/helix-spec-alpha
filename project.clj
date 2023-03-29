(defproject town.lilac/helix-spec-alpha "0.0.1"
  :description "clojure.spec.alpha support for React components defined with helix"
  :url "https://github.com/lilactown/helix-spec-alpha"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html"}
  :source-paths ["src"]
  :dependencies [[lilactown/helix "0.1.9"]
                 [org.clojure/spec.alpha "0.3.218"]]
  :deploy-repositories [["snapshots" {:sign-releases false
                                      :url "https://clojars.org"}]])
