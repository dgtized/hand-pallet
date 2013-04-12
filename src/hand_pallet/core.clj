(ns hand-pallet.core
  (:use [pallet.crate.automated-admin-user :only [automated-admin-user]])
  (:require pallet.core
            pallet.compute))

;; Define a vmfest-service for talking to virtualbox, this is
;; pulling configuration information from .pallet/config.clj and
;; combining it with the local pallet.clj.
(def vmfest-service (pallet.compute/instantiate-provider :vmfest))

;; Specify a node-spec for pallet to match to our images provided by vmfest
(def any-node (pallet.core/node-spec))

;; Given node-spec to find a matching image from the provider, we can
;; specify a group of 1 and install our ssh-key to a matching user.
(def debian-group
  (pallet.core/group-spec "hand-pallet"
                          :count 1
                          :node-spec any-node
                          :phases {:bootstrap automated-admin-user}))


