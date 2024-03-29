(ns hash-pw.core
  (:import [goog.crypt Sha256 Sha1 Md5])
  (:require [goog.crypt :as crypt]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)


(def hashes (sorted-map :sha256 #(Sha256.)
                        :sha1 #(Sha1.)
                        :md5 #(Md5.)))

(def app-state (atom {:password nil
                      :hash :sha256}))

(defn encode
  ([s]
   (encode s (-> @app-state :hash hashes)))
  ([s hash]
   (when s
     (let [sha (doto (hash)
                 (.update s))]
       (-> sha
           (.digest)
           (crypt/byteArrayToHex))))))

(defn pw-input-component [app owner]
  (reify
    om/IRenderState
    (render-state [_ state]
                  (dom/div #js {:className "row"}
                           (dom/div #js {:className "form-group"}
                                    (dom/label nil "Enter password")

                                    (dom/input #js {:type (if (:view-password? state) "text" "password")
                                                    :className "password form-control"
                                                    :ref "password"
                                                    :placeholder "Enter password"
                                                    :onChange (fn [e]
                                                                (let [pw (.. e -target -value)]
                                                                  (om/update! app :password pw)))}))
                           (dom/div #js {:className "checkbox"}
                                    (dom/label nil
                                               (dom/input #js {:type "checkbox"
                                                               :checked (:view-password? state)
                                                               :className ""
                                                               :onChange (fn [e]
                                                                           (let [view? (.. e -target -checked)]
                                                                             (om/set-state! owner :view-password? view?)))})
                                               "View password"))
                           (dom/div #js {:className "form-group"}
                                    (dom/label nil "Select Hash")

                                    (apply dom/select #js {:className "form-control"
                                                           :onChange (fn [e]
                                                                       (let [hash (.. e -target -value)]
                                                                         (om/update! app :hash (keyword hash))))
                                                           :value (name (:hash app))}
                                           (map
                                            (fn [[k v]]
                                              (dom/option #js {:value (name k)} (name k)))
                                            hashes)))
                           ))))

(om/root
  (fn [app owner]
    (reify om/IRender
      (render [_]
              (dom/div nil
                       (om/build pw-input-component app {:init-state {:view-password? false}})
                       (dom/h4 nil (-> app :password encode))))))
  app-state
  {:target (. js/document (getElementById "app"))})
