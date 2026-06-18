import { useNavigate } from "react-router-dom";
import { ArrowLeft, ShoppingBag, BookOpen, MapPin, CreditCard, Calendar, Package } from "lucide-react";
import { usePurchases } from "../components/PurchasesContext";

export function MyPurchases() {
    const navigate = useNavigate();
    const { purchases } = usePurchases();

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate(-1)}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Back
                </button>

                <div className="mb-8">
                    <h1 className="font-serif text-4xl mb-2 text-foreground flex items-center gap-3">
                        <ShoppingBag className="w-10 h-10 text-primary" />
                        My Purchases
                    </h1>
                    <p className="text-muted-foreground">
                        {purchases.length} {purchases.length === 1 ? "order" : "orders"} placed
                    </p>
                </div>

                {purchases.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <ShoppingBag className="w-16 h-16 mx-auto mb-4 text-muted-foreground opacity-50" />
                        <h2 className="font-serif text-2xl mb-2 text-foreground">No Purchases Yet</h2>
                        <p className="text-muted-foreground mb-6">
                            Browse the marketplace and complete a purchase to see your order history here.
                        </p>
                        <button
                            onClick={() => navigate("/explore")}
                            className="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                        >
                            Browse Marketplace
                        </button>
                    </div>
                ) : (
                    <div className="space-y-6">
                        {purchases.map(order => (
                            <div
                                key={order.orderId}
                                className="bg-white border-2 border-border rounded-2xl shadow-sm overflow-hidden"
                            >
                                {/* Order Header */}
                                <div className="bg-gradient-to-r from-primary/5 to-accent/5 border-b-2 border-border px-6 py-4 flex flex-wrap items-center justify-between gap-4">
                                    <div className="flex items-center gap-4">
                                        <div>
                                            <p className="text-xs text-muted-foreground uppercase tracking-wider font-semibold">Order ID</p>
                                            <p className="font-bold text-foreground">{order.orderId}</p>
                                        </div>
                                        <div className="hidden sm:block h-10 w-px bg-border" />
                                        <div className="hidden sm:block">
                                            <p className="text-xs text-muted-foreground uppercase tracking-wider font-semibold flex items-center gap-1">
                                                <Calendar className="w-3 h-3" /> Date
                                            </p>
                                            <p className="font-medium text-foreground">
                                                {order.date.toLocaleDateString("en-GB", {
                                                    day: "2-digit",
                                                    month: "short",
                                                    year: "numeric",
                                                    hour: "2-digit",
                                                    minute: "2-digit",
                                                })}
                                            </p>
                                        </div>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Package className="w-4 h-4 text-green-600" />
                                        <span className="text-sm font-semibold text-green-700 bg-green-100 px-3 py-1 rounded-full">
                      Purchased
                    </span>
                                        <span className="font-bold text-xl text-primary ml-2">€{order.total.toFixed(2)}</span>
                                    </div>
                                </div>

                                <div className="p-6 grid md:grid-cols-2 gap-6">
                                    {/* Items */}
                                    <div>
                                        <h3 className="font-semibold text-foreground mb-3 flex items-center gap-2">
                                            <BookOpen className="w-4 h-4 text-primary" /> Items
                                        </h3>
                                        <div className="space-y-3">
                                            {order.items.map(item => (
                                                <div
                                                    key={item.id}
                                                    className="flex items-center justify-between p-3 border-2 border-border rounded-xl bg-gray-50"
                                                >
                                                    <div className="flex items-center gap-3">
                                                        <div className="w-10 h-10 bg-gradient-to-br from-primary/10 to-accent/10 rounded-lg flex items-center justify-center flex-shrink-0">
                                                            <BookOpen className="w-5 h-5 text-primary/60" />
                                                        </div>
                                                        <span className="font-medium text-sm text-foreground line-clamp-2">{item.title}</span>
                                                    </div>
                                                    <span className="font-bold text-primary ml-2 whitespace-nowrap">€{item.price.toFixed(2)}</span>
                                                </div>
                                            ))}
                                        </div>
                                    </div>

                                    {/* Shipping & Payment */}
                                    <div className="space-y-4">
                                        <div>
                                            <h3 className="font-semibold text-foreground mb-2 flex items-center gap-2">
                                                <MapPin className="w-4 h-4 text-primary" /> Shipping Address
                                            </h3>
                                            <div className="p-3 border-2 border-border rounded-xl bg-gray-50 text-sm text-foreground leading-relaxed">
                                                {order.shipping.street}, no. {order.shipping.number}
                                                <br />
                                                {order.shipping.zipCode} — {order.shipping.city}
                                            </div>
                                        </div>

                                        <div>
                                            <h3 className="font-semibold text-foreground mb-2 flex items-center gap-2">
                                                <CreditCard className="w-4 h-4 text-primary" /> Payment
                                            </h3>
                                            <div className="p-3 border-2 border-border rounded-xl bg-gray-50 text-sm text-foreground font-mono tracking-wider">
                                                {order.maskedCard}
                                            </div>
                                        </div>

                                        <div className="flex justify-between items-center p-3 border-2 border-primary/20 rounded-xl bg-primary/5">
                                            <span className="font-semibold text-foreground">Total Charged</span>
                                            <span className="font-bold text-primary text-lg">€{order.total.toFixed(2)}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
