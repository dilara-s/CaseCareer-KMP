import Foundation

enum Formatters {

    // "150000" → "150 000 ₽"  (зеркало Ксюшиного formatReward)
    static func reward(_ raw: String) -> String {
        guard let amount = Double(raw) else { return "\(raw) ₽" }
        let long = Int(amount)
        let str = String(long)
        var result = ""
        str.reversed().enumerated().forEach { i, c in
            if i > 0 && i % 3 == 0 { result.append(" ") }
            result.append(c)
        }
        return String(result.reversed()) + " ₽"
    }

    // "2025-06-01T00:00:00Z" → "1 июня"  (зеркало Ксюшиного formatDeadline)
    static func deadline(_ raw: String) -> String {
        return shortDate(raw)
    }

    // "2025-04-10T12:00:00Z" → "10 апреля"
    static func shortDate(_ raw: String) -> String {
        let datePart = raw.components(separatedBy: "T").first ?? raw
        let parts = datePart.components(separatedBy: "-")
        guard parts.count >= 3,
              let day = Int(parts[2]) else { return raw }
        let month = monthName(parts[1])
        return "\(day) \(month)"
    }

    private static func monthName(_ num: String) -> String {
        switch num {
        case "01": return "января"
        case "02": return "февраля"
        case "03": return "марта"
        case "04": return "апреля"
        case "05": return "мая"
        case "06": return "июня"
        case "07": return "июля"
        case "08": return "августа"
        case "09": return "сентября"
        case "10": return "октября"
        case "11": return "ноября"
        case "12": return "декабря"
        default:   return ""
        }
    }
}
