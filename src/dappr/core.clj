(ns dappr.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [hiccup.core]))

(defn- get-method
  ([method]
   (get-method method {}))
  ([method params]
   (let [url "https://api.flickr.com/services/rest/"
         query (conj {"api_key" "302f8ca9844b5cb211cab1595c695631"
                      "format" "json"
                      "nojsoncallback" 1
                      "user_id" "79132105@N04"} params)]
     (client/get url {:query-params (conj query {"method" method})}))))

(defn- get-public-photos []
  (let [body (get (get-method "flickr.people.getPublicPhotos") :body)
        parsed-body (json/read-str body)
        photos (get (get parsed-body "photos") "photo")]
    photos))

(defn- get-sizes [photo-id]
  (->
    (get-method "flickr.photos.getSizes" {"photo_id" photo-id})
    (:body)
    (json/read-str)
    (get "sizes")
    (get "size")))

(defn- sizes->urls [sizes]
  (let [filter-fn (fn [label]
                    (fn [coll]
                      (=
                       (get coll "label")
                       label)))
        thumbnail-url (get
                        (first (filter (filter-fn "Thumbnail") sizes))
                        "source")
        large-url (get
                    (first (filter (filter-fn "Large") sizes))
                    "source")]
    {:thumbnail thumbnail-url
     :large large-url}))

(defn- get-data []
  (->>
    (get-public-photos)
    (map #(get % "id"))
    (map get-sizes)
    (map sizes->urls)))

(defn- urls->html [url-map]
  (html
    [:a
     {:href (url-map :large)}
     [:img
      {:src (url-map :thumbnail)}]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (get-data)))

;; make request to get all photos
;; get the ids of all photos
;; get the sizes of each photo
;; choose certain photo
