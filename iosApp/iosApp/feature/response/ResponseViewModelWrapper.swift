import Foundation
import Shared

final class ResponseViewModelWrapper: ObservableViewModel<ResponseState, ResponseEvent> {

    private let vm: ResponseViewModel

    init() {
        let vm = KoinHelper.shared.getResponseViewModel()
        self.vm = vm
        super.init(
            initialState: vm.state.value as! ResponseState,
            stateFlow: CommonFlowKt.asCommonFlow(vm.state) as! CommonFlow<ResponseState>
        )
    }

    func onEvent(_ event: ResponseEvent) {
        vm.onEvent(event: event)
    }

    func collectEffects(
        onClose: @escaping () -> Void,
        onNavigateToMyCases: @escaping () -> Void
    ) {
        CommonFlowKt.asCommonFlow(vm.effect).watch { effect in
            DispatchQueue.main.async {
                if effect is ResponseEffectCloseSheet {
                    onClose()
                } else if effect is ResponseEffectNavigateToMyCases {
                    onNavigateToMyCases()
                }
            }
        }
    }
}
