;; Specify the vmfest service provider

(defpallet
  :services {:vmfest {:provider "vmfest"
                      :default-network-type :local
                      :default-bridged-interface "wlan1"
                      :default-local-interface "vboxnet0"}})
