import SwiftUI
struct ContentView: View {
    @State private var showingSettingsSheet = false
    @State private var navigateToQuestions = false

    var body: some View {
        NavigationView {
            ZStack {
                
                Color(red: 0 / 255, green: 216 / 255, blue: 255 / 255)
                .edgesIgnoringSafeArea(.all)
                
                // 背景画像
                Image("Image")
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .edgesIgnoringSafeArea(.all)
                
                VStack {
                    NavigationLink(destination: QuestionsView(viewModel: QuestionsViewModel()), isActive: $navigateToQuestions) {
                        EmptyView()
                    }
                    .navigationBarItems(trailing: Button(action: {
                        self.showingSettingsSheet = true
                    }) {
                        Image(systemName: "gear") // 歯車アイコン
                    })
                    .sheet(isPresented: $showingSettingsSheet) {
                        SettingsSheetView(navigateToQuestions: $navigateToQuestions)
                    }
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
    
struct SettingsSheetView: View {
    @Environment(\.presentationMode) var presentationMode
    @Binding var navigateToQuestions: Bool
    @State private var passcode = ""
    @State private var showError = false
    
    var body: some View {
        VStack(spacing: 20) {
            TextField("あんしば創立記念月日4桁", text: $passcode)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            if showError {
                Text("パスワードを正しく入力してください")
                    .foregroundColor(.red)
            }
            
            Button("送信") {
                if passcode == "0614" {
                    // パスコードが正しい場合の処理
                    presentationMode.wrappedValue.dismiss()
                    navigateToQuestions = true
                } else {
                    showError = true
                }
            }
        }
        .padding()
    }
}



struct QuestionsView: View {
    @ObservedObject var viewModel: QuestionsViewModel
    @State private var showingAddQuestionSheet = false
    @State private var showingLocationList = false
    @State private var showingGenreList = false

    var body: some View {
        NavigationView {
            List(viewModel.questions) { question in
                NavigationLink(destination: UserSelectionView(viewModel: viewModel, question: question)) {
                    Text(question.name)
                }
            }
            .navigationBarTitle("開発者モード問題一覧")
            .navigationBarItems(
                leading: Button(action: {
                    viewModel.fetchQuestions()
                }) {
                    Text("更新")
                },
                trailing: HStack {
                    
                    // 問題追加ボタン
                    Button(action: {
                        showingAddQuestionSheet = true
                    }) {
                        Image(systemName: "plus")
                    }
                }
            )
            .sheet(isPresented: $showingAddQuestionSheet) {
                AddQuestionView(viewModel: viewModel)
            }
        }
    }
}


struct UserSelectionView: View {
@ObservedObject var viewModel: QuestionsViewModel
var question: Question
@State private var showingEditView = false // シート表示用の状態変数

var body: some View {
    NavigationView {
        List(viewModel.users) { user in
            NavigationLink(destination: QuestionDetailView(question: question, userID: user.id, viewModel: viewModel)) {
                Text(user.name)
            }
        }
        .navigationBarTitle("ユーザ選択")
        .navigationBarItems(
            leading: Button(action: {
            viewModel.fetchUsers() 
        }) {
            Image(systemName: "arrow.clockwise") // 更新アイコン
        },
            trailing: Button(action: {
                showingEditView = true
            }) {
                Text("問題内容変更")
            }
        )
        .sheet(isPresented: $showingEditView) {
            EditQuestionView(question: question, viewModel: viewModel)
        }
    }
}
}



struct QuestionDetailView: View {
var question: Question
var userID: Int
@ObservedObject var viewModel: QuestionsViewModel
@State private var showingEditView = false
@State private var showingAlert = false
@State private var alertMessage = ""

var body: some View {
    VStack {

        Text(question.name)
        
        switch viewModel.userQuestionDataStatus {
        case .notFetched:
            Text("データを取得中...(T/F更新を押してね)")
        case .exists(let value):
            Text(value ? "True　(ユーザ変更時はT/F更新を押してね)" : "False(ユーザ変更時はT/F更新を押してね)")
        case .notExists:
            Text("T/Fデータなし(ユーザ変更時はT/F更新を押してね)")
        }
        
        // Trueにするボタン
        Button("Trueにする") {
            checkDataAndRegister(cor: true)
        }
        
        // Falseにするボタン
        Button("Falseにする") {
            checkDataAndRegister(cor: false)
        }
        .alert(isPresented: $showingAlert) {
            Alert(title: Text("エラー"), message: Text(alertMessage), dismissButton: .default(Text("OK")))
        }
        
        // リセットボタン
        Button("リセット(一回リセットしてからTF変更してね)") {
            viewModel.deleteUserQuestionData(for: question.id!,userID: userID)
        }
    }
    .navigationBarItems(trailing: Button("T/F更新") {
        self.viewModel.fetchUserQuestionDataId(for: question.id!, userID: userID)
    })
}

// データ存在チェックと登録処理の関数
private func checkDataAndRegister(cor: Bool) {
    viewModel.checkDataExistence(for: question.id!, userID: userID) { exists in
        DispatchQueue.main.async {
            if exists {
                self.alertMessage = "データが既に存在します。"
                self.showingAlert = true
            } else {
                self.viewModel.updateUserQuestionData(questionID: self.question.id!, cor: cor, userID: self.userID)
            }
        }
    }
}

}

struct AddQuestionView: View {
    @ObservedObject var viewModel: QuestionsViewModel
    @State private var name: String = ""
    @State private var txt: String = ""
    @State private var link: String = ""
    @State private var locId: Int = 0
    @State private var genreId: Int = 0
    @State private var showingImagePicker = false
    @State private var inputImage: UIImage?
    @State private var imagePreview: Image?

    var body: some View {
        NavigationView {
            Form {
                TextField("名前", text: $name)
                TextField("詳細な説明文", text: $txt)
                TextField("リンク", text: $link)
                TextField("Location ID", value: $locId, formatter: NumberFormatter())
                TextField("Genre ID", value: $genreId, formatter: NumberFormatter())
                
                Button("画像を選択") {
                    showingImagePicker = true
                }
                
                if let imagePreview = imagePreview {
                    imagePreview
                        .resizable()
                        .scaledToFit()
                        .frame(height: 200)
                }
                
                Button("作成") {
                    if let inputImage = inputImage, let imgData = inputImage.jpegData(compressionQuality: 1) {
                        let newQuestion = Question(name: name, img: imgData, txt: txt, link: link, locId: locId, genreId: genreId)
                        viewModel.addQuestion(question: newQuestion)
                    }
                }
            }
            .sheet(isPresented: $showingImagePicker, onDismiss: loadImage) {
                ImagePicker(image: self.$inputImage)
            }
            .navigationBarTitle("質問を追加", displayMode: .inline)
        }
    }
    
    func loadImage() {
        guard let inputImage = inputImage else { return }
        imagePreview = Image(uiImage: inputImage)
    }
}


struct EditQuestionView: View {
    @ObservedObject var viewModel: QuestionsViewModel
    var question: Question
    @State private var name: String
    @State private var txt: String
    @State private var link: String
    @State private var locId: Int
    @State private var genreId: Int
    @State private var showingImagePicker = false
    @State private var inputImage: UIImage?
    @State private var imagePreview: Image?
    @Environment(\.presentationMode) var presentationMode

    init(question: Question, viewModel: QuestionsViewModel) {
        self.question = question
        self.viewModel = viewModel
        _name = State(initialValue: question.name)
        _txt = State(initialValue: question.txt)
        _link = State(initialValue: question.link ?? "")
        _locId = State(initialValue: question.locId)
        _genreId = State(initialValue: question.genreId)
    }
    
    var body: some View {
        NavigationView {
            Form {
                TextField("名前", text: $name)
                TextField("詳細な説明文", text: $txt)
                TextField("リンク", text: $link)
                TextField("Location ID", value: $locId, formatter: NumberFormatter())
                TextField("Genre ID", value: $genreId, formatter: NumberFormatter())
                
                Button("画像を選択") {
                    showingImagePicker = true
                }
                
                if let imagePreview = imagePreview {
                    imagePreview
                        .resizable()
                        .scaledToFit()
                        .frame(height: 200)
                }
                
                Button("変更") {
                    if let inputImage = inputImage, let imgData = inputImage.jpegData(compressionQuality: 1) {
                        let updatedQuestion = Question(id: question.id, name: name, img: imgData, txt: txt, link: link, locId: locId, genreId: genreId)
                        viewModel.editQuestion(question: updatedQuestion) { success, error in
                            if success {
                                presentationMode.wrappedValue.dismiss()
                            }
                        }
                    }
                }
            }
            .sheet(isPresented: $showingImagePicker, onDismiss: loadImage) {
                ImagePicker(image: self.$inputImage)
            }
            .navigationBarTitle("質問を編集", displayMode: .inline)
        }
    }
    
    func loadImage() {
        guard let inputImage = inputImage else { return }
        imagePreview = Image(uiImage: inputImage)
    }
}
