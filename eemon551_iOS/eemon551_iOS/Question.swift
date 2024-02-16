import Foundation

struct Question: Codable, Identifiable {
    var id: Int? // Optional because a new question might not have an ID until it's saved on the server
    var name: String
    var txt: String
    var link: String
    var locId: Int
    var genreId: Int
    var img: Data? // Assuming this is the correct type based on your previous messages
}

