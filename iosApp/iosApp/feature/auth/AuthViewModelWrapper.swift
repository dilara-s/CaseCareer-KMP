import Foundation
import Shared

final class AuthViewModelWrapper: ObservableViewModel<AuthState, AuthEvent> {

    private let vm: AuthViewModel

    init() {
        let vm = KoinHelper.shared.getAuthViewModel()
        self.vm = vm
        super.init(
            initialState: vm.state.value as! AuthState,
            stateFlow: CommonFlowKt.asCommonFlow(vm.state) as! CommonFlow<AuthState>
        )
    }

    func onEvent(_ event: AuthEvent) {
        vm.onEvent(event: event)
    }

    func collectEffects(onNavigateToMain: @escaping () -> Void) {
        CommonFlowKt.asCommonFlow(vm.effect).watch { effect in
            DispatchQueue.main.async {
                if effect is AuthEffectNavigateToMain {
                    onNavigateToMain()
                }
            }
        }
    }
}
