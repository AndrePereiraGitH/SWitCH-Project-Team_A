import { Check } from "lucide-react";

export function SuccessModal({ isOpen, onClose, message }) {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
            <div className="bg-white rounded-3xl p-10 max-w-md w-full shadow-2xl transform animate-in zoom-in duration-300">
                <div className="text-center">
                    <div className="mb-6 relative">
                        <div className="relative inline-flex items-center justify-center w-24 h-24 bg-gradient-to-br from-green-400 to-emerald-500 rounded-full shadow-xl">
                            <Check className="w-12 h-12 text-white" strokeWidth={3} />
                        </div>
                    </div>
                    <h2 className="font-serif text-3xl mb-3 text-foreground bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
                        Success!
                    </h2>
                    <p className="text-lg text-muted-foreground mb-8 leading-relaxed">{message}</p>
                    <button
                        onClick={onClose}
                        className="w-full px-8 py-4 bg-gradient-to-r from-green-500 to-emerald-500 text-white rounded-xl hover:shadow-2xl hover:scale-105 transition-all font-semibold text-lg"
                    >
                        OK
                    </button>
                </div>
            </div>
        </div>
    );
}