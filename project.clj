(defproject lein-blacklist "0.1.0"
  :description "Detect blacklisted namespaces being required in source."
  :url "https://github.com/technomancy/lein-blacklist"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true
  ;; Running `lein blacklist` on this codebase should complain about
  ;; the first two entries here, but not the second.
  :blacklist {#"^clojure.string$" []
              #"^bultitude" [#"non.exist" #"burgers"]
              #"^leiningen\.main" [#"^leiningen"]})
