struct Question: Codable, Identifiable {
    var id: Int?
    var name: String
    var img: String
    var txt: String
    var link: String
    var loc_id: Int
    var genre_id: Int
}

