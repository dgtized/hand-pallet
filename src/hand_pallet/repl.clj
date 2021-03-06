(ns hand-pallet.repl
  (:require [hand-pallet.core :as hpc]
            pallet.core
            pallet.compute
            pallet.compute.vmfest
            vmfest.manager))

(comment
  ;; If the lein pallet add-vmfest-image command failed we can install
  ;; the image using the repl. This takes a while to download.
  (pallet.compute.vmfest/add-image
   hpc/vmfest-service
   "https://s3.amazonaws.com/vmfest-images/debian-6.0.2.1-64bit-v0.3.vdi.gz")

  ;; Verify vmfest can find the debian image we just added
  (vmfest.manager/models)
  ;; => (:debian-6.0.2.1-64bit-v0.3)

  ;; Spin up one hand-pallet vm using the matching node. Since this is
  ;; a group-spec, it will be labeled hand-pallet-0 in the VirtualBox
  ;; gui.
  (pallet.core/converge (assoc hpc/debian-group :count 1)
                        :compute hpc/vmfest-service)
  ;; Now we wait for hand-pallet-0 to boot

  ;; Check your *nrepl-server* log, but we should have a running
  ;; machine, query the nodes to verify this.
  (pallet.compute/nodes hpc/vmfest-service)
  ;; => (hand-pallet-0 hand-pallet	public: 192.168.1.212)

  ;; After finding the public IP from the above command, we should now
  ;; be able to ssh in using password vmfest.
  ;;
  ;;   $ ssh vmfest@192.168.1.212
  ;;

  ;; In order to remove the node we can execute the following code that
  ;; we have commented out.
  (pallet.core/converge (assoc hpc/debian-group :count 0)
                        :compute hpc/vmfest-service)
  )
