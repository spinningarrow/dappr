(ns phobos.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clostache.parser :as mustache]
            [clojure.walk :as walk]))

(defn- flickr-method
  [method params]
  (let [api-url "https://api.flickr.com/services/rest/"
        default-params {"api_key" "302f8ca9844b5cb211cab1595c695631"
                        "format" "json"
                        "nojsoncallback" 1}
        query-params (conj default-params params {"method" method})
        result (client/get api-url {:query-params query-params})
        body (json/read-str (result :body))]
    body))

(defn- public-photos
  "Get the public photos for the specified user"
  [user_id]
  (let [body (flickr-method
               "flickr.people.getPublicPhotos"
               {"user_id" user_id
                "extras" "url_l"})
        photos (-> body (get "photos") (get "photo"))]
    photos))

(defn- photoset-photos
  "Get the photos from a specified photoset (adding the user_id improves
  performance according to the Flickr API docs)"
  [photoset_id user_id]
  (let [body (flickr-method
               "flickr.photosets.getPhotos"
               {"user_id" user_id
                "photoset_id" photoset_id
                "extras" "url_l,date_taken,path_alias"})
        photos (-> body (get "photoset") (get "photo"))]
    photos))

(defn- generate
  "Generates the output given some photos data and a template path"
  [data input-file output-file]
  (let [photos (map walk/keywordize-keys data)
        output (mustache/render-resource input-file {:photos photos})]
    (spit output-file output)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (generate
    (photoset-photos "72157659937187072" "79132105@N04")
    "templates/photoblog.mustache.html"
    "src/templates/photoblog.html"))
