import Foundation
import Shared

final class ProfileViewModelWrapper: ObservableViewModel<ProfileState, ProfileEvent> {

    private let vm: ProfileViewModel

    init() {
        let vm = KoinHelper.shared.getProfileViewModel()
        self.vm = vm
        super.init(
            initialState: vm.state.value as! ProfileState,
            stateFlow: CommonFlowKt.asCommonFlow(vm.state) as! CommonFlow<ProfileState>
        )
    }

    func onEvent(_ event: ProfileEvent) {
        vm.onEvent(event: event)
    }

    func collectEffects(onNavigateToAuth: @escaping () -> Void) {
        CommonFlowKt.asCommonFlow(vm.effect).watch { effect in
            DispatchQueue.main.async {
                if effect is ProfileEffectNavigateToAuth {
                    onNavigateToAuth()
                }
            }
        }
    }
}
