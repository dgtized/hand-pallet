;; Specify the vmfest service provider

(defpallet
  :services {:vmfest {:provider "vmfest"
                      :default-network-type :bridged
                      :default-bridged-interface "wlan1"}})
