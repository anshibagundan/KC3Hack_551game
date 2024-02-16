import Foundation

struct Question: Codable, Identifiable {
    var id: Int?
    var name: String
    var txt: String
    var link: String?
    var locId: Int
    var genreId: Int
    var img: Data? // Ensure Data type is recognized with import Foundation

    enum CodingKeys: String, CodingKey {
        case id, name, txt, link, locId, genreId, img
    }

    // Custom decoder to handle the Base64-encoded image
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decodeIfPresent(Int.self, forKey: .id)
        name = try container.decode(String.self, forKey: .name)
        txt = try container.decode(String.self, forKey: .txt)
        link = try container.decodeIfPresent(String.self, forKey: .link)
        locId = try container.decode(Int.self, forKey: .locId)
        genreId = try container.decode(Int.self, forKey: .genreId)

        if let imgBase64 = try container.decodeIfPresent(String.self, forKey: .img) {
            img = Data(base64Encoded: imgBase64)
        } else {
            img = nil
        }
    }

    // Custom encoder to handle the Base64-encoded image
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        // Encode other properties
        if let imgData = img {
            let imgBase64String = imgData.base64EncodedString()
            try container.encode(imgBase64String, forKey: .img)
        } else {
            try container.encode(nil as String?, forKey: .img)
        }
    }
}

