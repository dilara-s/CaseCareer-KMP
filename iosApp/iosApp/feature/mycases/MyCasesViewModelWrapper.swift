import Foundation
import Shared

final class MyCasesViewModelWrapper: ObservableViewModel<MyCasesState, MyCasesEvent> {

    private let vm: MyCasesViewModel

    init() {
        let vm = KoinHelper.shared.getMyCasesViewModel()
        self.vm = vm
        super.init(
            initialState: vm.state.value as! MyCasesState,
            stateFlow: CommonFlowKt.asCommonFlow(vm.state) as! CommonFlow<MyCasesState>
        )
    }

    func onEvent(_ event: MyCasesEvent) {
        vm.onEvent(event: event)
    }

    // MyCasesEffect.NavigateToCaseDetail.caseId = Kotlin Int (Int32)
    // CaseDetailView принимает Int64 — кастуем при передаче
    func collectEffects(onOpenCase: @escaping (Int64) -> Void) {
        CommonFlowKt.asCommonFlow(vm.effect).watch { effect in
            DispatchQueue.main.async {
                if let e = effect as? MyCasesEffectNavigateToCaseDetail {
                    onOpenCase(Int64(e.caseId))
                }
            }
        }
    }
}
