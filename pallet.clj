;; Give access to the namespace where debian-goup is specified so it
;; can be used below
(require
 '[hand-pallet.core :refer [debian-group]])

(defproject hand-pallet
  :provider {:vmfest
             {:variants [{:node-spec {:image {:os-64-bit true}}
                          :selectors #{:default}}
                         {:node-spec
                          {:image {:os-family :debian
                                   :os-64-bit true}}
                          :selectors #{:debian}}
                         {:node-spec
                          {:image {:os-family :ubuntu
                                   :os-64-bit true}}
                          :selectors #{:ubuntu}}]}}
  ;; :groups specifies to `lein pallet` which nodes to converge by
  ;; default when executing `lein pallet up`.
  :groups [debian-group])

