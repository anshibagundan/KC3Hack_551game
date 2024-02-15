//
//  Item.swift
//  eemon551_iOS
//
//  Created by 棚橋柊太 on 2024/02/14.
//

import Foundation
import SwiftData

@Model
final class Item {
    var timestamp: Date
    
    init(timestamp: Date) {
        self.timestamp = timestamp
    }
}
