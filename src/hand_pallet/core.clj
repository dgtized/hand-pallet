(ns hand-pallet.core
  (:use [pallet.crate.automated-admin-user :only [automated-admin-user]])
  (:require pallet.core
            pallet.compute))

;; Define a vmfest-service for talking to virtualbox, this is
;; pulling configuration information from .pallet/config.clj and
;; combining it with the local pallet.clj.
(def vmfest-service (pallet.compute/instantiate-provider :vmfest))

;; Specify a node-spec for pallet to match to our images provided by vmfest
(def debian-node (pallet.core/node-spec :image {:os-family :debian}))

(def debian-group
  (pallet.core/group-spec "hand-pallet"
                          :count 1
                          :node-spec debian-node
                          :phases {:bootstrap automated-admin-user}))

;; Note that executing:
;;
;;   $ lein pallet down
;;
;; Also appears to accomplish this, unfortunately not sure how to
;; get lein pallet up to work equivalently.


