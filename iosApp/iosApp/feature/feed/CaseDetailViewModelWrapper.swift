import Foundation
import Shared

final class CaseDetailViewModelWrapper: ObservableViewModel<CaseDetailState, CaseDetailEvent> {

    private let vm: CaseDetailViewModel

    init(caseId: Int64) {
        let vm = KoinHelper.shared.getCaseDetailViewModel(caseId: caseId)
        self.vm = vm
        super.init(
            initialState: vm.state.value as! CaseDetailState,
            stateFlow: CommonFlowKt.asCommonFlow(vm.state) as! CommonFlow<CaseDetailState>
        )
    }

    func onEvent(_ event: CaseDetailEvent) {
        vm.onEvent(event: event)
    }

    func collectEffects(
        onNavigateBack: @escaping () -> Void,
        onNavigateToNda: @escaping (Int64) -> Void,
        onNavigateToApply: @escaping (Int64) -> Void
    ) {
        // CaseDetailEffect — sealed class → dot-нотация работает
        CommonFlowKt.asCommonFlow(vm.effect).watch { effect in
            DispatchQueue.main.async {
                if effect is CaseDetailEffect.NavigateBack {
                    onNavigateBack()
                } else if let e = effect as? CaseDetailEffect.NavigateToNdaStep {
                    onNavigateToNda(e.caseId)
                } else if let e = effect as? CaseDetailEffect.NavigateToApplyStep {
                    onNavigateToApply(e.caseId)
                }
            }
        }
    }
}
