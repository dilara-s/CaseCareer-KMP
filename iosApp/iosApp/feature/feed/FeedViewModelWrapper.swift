import Foundation
import Shared

final class FeedViewModelWrapper: ObservableViewModel<FeedState, FeedEvent> {

    private let vm: FeedViewModel

    init() {
        let vm = KoinHelper.shared.getFeedViewModel()
        self.vm = vm
        super.init(
            initialState: vm.state.value as! FeedState,
            stateFlow: CommonFlowKt.asCommonFlow(vm.state) as! CommonFlow<FeedState>
        )
    }

    func onEvent(_ event: FeedEvent) {
        vm.onEvent(event: event)
    }

    func collectEffects(onNavigateToCaseDetail: @escaping (Int64) -> Void) {
        CommonFlowKt.asCommonFlow(vm.effect).watch { effect in
            DispatchQueue.main.async {
                if let e = effect as? FeedEffectNavigateToCaseDetail {
                    onNavigateToCaseDetail(e.caseId)
                }
            }
        }
    }
}
