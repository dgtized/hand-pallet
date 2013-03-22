(ns hand-pallet.core
  (:use [pallet.crate.automated-admin-user :only [automated-admin-user]])
  (:require pallet.core
            pallet.compute
            pallet.compute.vmfest
            pallet.configure
            pallet.algo.fsmop
            vmfest.manager))

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

(def debian-image
  "https://s3.amazonaws.com/vmfest-images/debian-6.0.2.1-64bit-v0.3.vdi.gz")

;; Define a vmfest-service for talking to virtualbox, this is
;; pulling configuration information from .pallet/config.clj and
;; combining it with the local pallet.clj.
(def vmfest-service (pallet.configure/compute-service :vmfest))

;; Download the debian-image, and wait a while for it to install
(pallet.compute.vmfest/add-image vmfest-service debian-image)

;; Verify vmfest can find the debian image we just added
(vmfest.manager/models)
;; => (:debian-6.0.2.1-64bit-v0.3)

;; Specify a node-spec for pallet to match to our images provided by vmfest
(def debian-node (pallet.core/node-spec :image {:os-family :debian}))

(def debian-group
  (pallet.core/group-spec "hand-pallet"
                          :count 1
                          :node-spec debian-node
                          :phases {:bootstrap automated-admin-user}))

;; Spin up one hand-pallet node using the matching node. Since this is
;; a group-spec, it will be labeled hand-pallet-0 in the VirtualBox
;; gui. Note that the let and wait-for are just so we can eval this
;; whole buffer and know we are in a clean state once the ip address
;; prints.
(comment
  (let [op
        (pallet.core/converge (assoc debian-group :count 1)
                              :compute vmfest-service)]
    (pallet.algo.fsmop/wait-for op)
    ;; Check you *nrepl-server* log, but we should have a running
    ;; machine, query the nodes to verify this.
    (println (pallet.compute/nodes vmfest-service))
    ;; => (hand-pallet-0 hand-pallet	public: 192.168.1.212)
    ))

;; After finding the public IP from the above command, we should now
;; be able to ssh in using password vmfest.
;;
;;   $ ssh vmfest@192.168.1.212
;;

;; In order to remove the node we can execute the following code that
;; we have commented out.
(comment
  (pallet.core/converge (assoc debian-group :count 0) :compute vmfest-service))

;; Note that executing:
;;
;;   $ lein pallet down
;;
;; Also appears to accomplish this, unfortunately not sure how to
;; get lein pallet up to work equivalently.


