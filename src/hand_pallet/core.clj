(ns hand-pallet.core
  (:use [pallet.crate.automated-admin-user :only [automated-admin-user]])
  (:require pallet.core
            pallet.compute))

;; I started this tutorial by executing;
;;
;;   $ lein new hand-pallet
;;   $ cd hand-pallet
;;   $ lein pallet project-init
;;
;; This adds our starting project.clj, and pallet.clj in the root,
;; which I have since modified to add dependencies for pallet and
;; specify that pallet should use virtualbox as it's provider by way
;; of vmfest.

;; At this point you can execute:
;;
;;   $ lein pallet config
;;
;; But I updated this config to what is in pallet-config.clj, so copy
;; that to ~/.pallet/config.clj. You may need to adjust the
;; :default-network-type and :default-bridged-interface per the
;; documentation at
;; https://github.com/pallet/pallet-vmfest#configuration.

;; To download a vmfest image execute:

;;   $ lein pallet add-vmfest-image \
;;          https://s3.amazonaws.com/vmfest-images/debian-6.0.2.1-64bit-v0.3.vdi.gz

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


