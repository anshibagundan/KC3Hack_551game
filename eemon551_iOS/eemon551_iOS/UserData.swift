import Foundation

struct UserData: Identifiable, Codable {
    var id: Int
    var name: String
    var score: Int
    var combo: Int
}
