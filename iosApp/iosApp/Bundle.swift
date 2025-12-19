import Foundation

extension Bundle {
    var appName: String {
        if let displayName = object(forInfoDictionaryKey: "CFBundleDisplayName") as? String {
            return displayName
        }
        // Fallback to CFBundleName
        if let name = object(forInfoDictionaryKey: "CFBundleName") as? String {
            return name
        }
        return "App"
    }
}
