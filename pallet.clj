(defproject hand-pallet
  :provider {:vmfest
             {:node-spec
              {:image {:os-family :debian
                       :os-64-bit true}}
              :selectors #{:default}}})
