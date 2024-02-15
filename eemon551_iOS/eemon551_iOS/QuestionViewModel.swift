import Foundation

class QuestionsViewModel: ObservableObject {
    @Published var questions: [Question] = []
    @Published var users: [UserData] = []
    @Published var message: String = "Question loading..."
    @Published var showingAddQuestionSheet = false
    
    
    
    enum UserQuestionDataStatus {
        case notFetched // データがまだフェッチされていない
        case exists(value: Bool) // データが存在し、その値（true/false）
        case notExists // データが存在しない
    }
    
    @Published var userQuestionDataStatus: UserQuestionDataStatus = .notFetched
    
    
    func fetchUsers() {
        let url = URL(string: "https://eemon-551.onrender.com/userdatas/")!
        let request = URLRequest(url: url)
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let data = data, let decodedResponse = try? JSONDecoder().decode([UserData].self, from: data) {
                DispatchQueue.main.async {
                    self.users = decodedResponse
                }
            } else {
                DispatchQueue.main.async {
                    self.message = "Network Error"
                }
            }
        }.resume()
    }
    
    
    
    func fetchQuestions() {
        guard let url = URL(string: "https://eemon-551.onrender.com/questions/") else { return }
        
        URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data, let response = response as? HTTPURLResponse, response.statusCode == 200 {
                if let decodedResponse = try? JSONDecoder().decode([Question].self, from: data) {
                    DispatchQueue.main.async {
                        self.questions = decodedResponse
                    }
                }
            } else {
                DispatchQueue.main.async {
                    self.message = "Network Error"
                }
            }
        }.resume()
    }
    //開発者用の質問追加，編集，削除
    func addQuestion(question: Question) {
        let postURL = URL(string: "https://eemon-551.onrender.com/questions/")!
        var request = URLRequest(url: postURL)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        do {
            let jsonData = try JSONEncoder().encode(question)
            request.httpBody = jsonData
            
            URLSession.shared.dataTask(with: request) { _, response, error in
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 200 {
                    DispatchQueue.main.async {
                        print("Question added successfully")
                    }
                } else if error != nil {
                    print("Error adding question. Status code: \((response as? HTTPURLResponse)?.statusCode ?? -1)")
                }
            }.resume()
        } catch {
            print("Error encoding question: \(error)")
        }
    }
    
    
    func deleteQuestion(id: Int) {
        guard let url = URL(string: "https://eemon-551.onrender.com/questions/\(id)/") else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            guard let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 200 else {
                print("Error deleting question. Status code: \((response as? HTTPURLResponse)?.statusCode ?? -1)")
                return
            }
            // 成功した場合の処理をここに書く
            print("Question deleted successfully")
        }.resume()
    }
    
    func editQuestion(question: Question) {
        guard let url = URL(string: "https://eemon-551.onrender.com/questions/\(question.loc_id)/") else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "PUT" // 編集は通常PUTまたはPATCHメソッドを使用
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        do {
            let jsonData = try JSONEncoder().encode(question)
            request.httpBody = jsonData
            
            URLSession.shared.dataTask(with: request) { _, response, error in
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 200 {
                    DispatchQueue.main.async {
                        print("Question edited successfully")
                    }
                } else if error != nil {
                    print("Error editing question. Status code: \((response as? HTTPURLResponse)?.statusCode ?? -1)")
                }
            }.resume()
        } catch {
            print("Error encoding question for update: \(error)")
        }
    }
    
    //開発者用のtrue false登録
    func checkDataExistence(for questionID: Int, userID: Int, completion: @escaping (Bool) -> Void) {
        // URLを修正して、userIDもクエリパラメータに含める
        let fetchURL = URL(string: "https://eemon-551.onrender.com/userquestiondatas?qes_id=\(questionID)&user_data_id=\(userID)")!
        
        URLSession.shared.dataTask(with: fetchURL) { data, response, error in
            guard let data = data,
                  let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 200,
                  error == nil else {
                print("Error fetching user question data ID.")
                completion(false)
                return
            }
            
            do {
                let userQuestionDatas = try JSONDecoder().decode([UserQuestionData].self, from: data)
                // userIDとquestionIDの組み合わせでデータが存在するかどうかをチェック
                completion(!userQuestionDatas.isEmpty)
            } catch {
                print("Error decoding user question data: \(error)")
                completion(false)
            }
        }.resume()
    }
    
    
    func updateUserQuestionData(questionID: Int, cor: Bool, userID: Int) {
        let postURL = URL(string: "https://eemon-551.onrender.com/userquestiondatas/")!
        var request = URLRequest(url: postURL)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let postData = UserQuestionData(cor: cor, user_data_id: userID, qes_id: questionID)
        do {
            let jsonData = try JSONEncoder().encode(postData)
            request.httpBody = jsonData
            
            URLSession.shared.dataTask(with: request) { _, response, error in
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 200 {
                    print("User question data added successfully")
                } else if let error = error {
                    print("Error adding user question data: \(error.localizedDescription)")
                } else {
                    print("Unexpected status code: \((response as? HTTPURLResponse)?.statusCode ?? -1)")
                }
            }.resume()
        } catch {
            print("Error encoding post data: \(error.localizedDescription)")
        }
    }
    
    
    func fetchUserQuestionDataId(for questionID: Int, userID: Int) {
        let fetchURL = URL(string: "https://eemon-551.onrender.com/userquestiondatas?qes_id=\(questionID)&user_data_id=\(userID)")!
        URLSession.shared.dataTask(with: fetchURL) { data, response, error in
            guard let data = data,
                  let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 200,
                  error == nil else {
                DispatchQueue.main.async {
                    // エラーが発生した場合は、データが存在しないとみなし、適切な状態を設定
                    self.userQuestionDataStatus = .notExists
                }
                print("Error fetching user question data for userID: \(userID) and questionID: \(questionID).")
                return
            }
            
            do {
                let userQuestionDatas = try JSONDecoder().decode([UserQuestionData].self, from: data)
                // データが取得できた場合、最初のデータを基に状態を更新（通常は1つのデータのみが期待される）
                if let firstUserQuestionData = userQuestionDatas.first {
                    DispatchQueue.main.async {
                        // ユーザーと問題IDの組み合わせに対するデータが存在する場合、そのcor値に基づいて状態を更新
                        self.userQuestionDataStatus = .exists(value: firstUserQuestionData.cor)
                    }
                } else {
                    // データが空の場合、データが存在しないとみなし、状態を更新
                    DispatchQueue.main.async {
                        self.userQuestionDataStatus = .notExists
                    }
                }
            } catch {
                DispatchQueue.main.async {
                    // デコード中にエラーが発生した場合も、データが存在しないとみなし、状態を更新
                    self.userQuestionDataStatus = .notExists
                }
                print("Error decoding user question data: \(error)")
            }
        }.resume()
    }
    
    
    func deleteUserQuestionData(for questionID: Int, userID: Int) {
        guard let url = URL(string: "https://eemon-551.onrender.com/userquestiondatas?qes_id=\(questionID)&user_data_id=\(userID)") else {
            print("Invalid URL")
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode == 204 {
                // 成功した場合、ステータスコード204 (No Content) が期待されます
                print("Successfully deleted user question data.")
            } else {
                // エラーレスポンスを処理
                if let httpResponse = response as? HTTPURLResponse {
                    print("Error deleting user question data. Status code: \(httpResponse.statusCode)")
                } else {
                    print("Error deleting user question data. No response from server.")
                }
            }
        }.resume()
    }
}
