import Foundation
import Shared

// Базовый класс: подписывается на StateFlow<T> из Kotlin и публикует изменения в SwiftUI
// T — тип State (ProfileState, MyCasesState, ResponseState)
// State: AnyObject — Kotlin/Native требует class type для generic параметра CommonFlow<T>
class ObservableViewModel<State: AnyObject, Event>: ObservableObject {

    @Published private(set) var state: State
    private var cancelable: Cancelable?

    init(initialState: State, stateFlow: CommonFlow<State>) {
        self.state = initialState
        self.cancelable = stateFlow.watch { [weak self] newState in
            DispatchQueue.main.async {
                guard let self, let newState else { return }
                self.state = newState
            }
        }
    }

    deinit {
        cancelable?.cancel()
    }
}
