;; Specify the vmfest service provider

(defpallet
  :services {:vmfest {:provider "vmfest"
                      :default-network-type :local
                      :default-bridged-interface "vboxnet0"}})
