import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, ShoppingCart, Trash2, CheckCircle, CreditCard, MapPin, Mail } from "lucide-react";
import { useCart } from "../components/CartContext";
import { usePurchases } from "../components/PurchasesContext";
import { createTransaction, getCities, markListingAsSold, CURRENT_USER_ID } from "../../api/transactions";

const emptyShipping = { street: "", number: "", zipCode: "", city: "" };
const emptyPayment = { cardHolderName: "", cardNumber: "", expiryMonth: "", expiryYear: "" };

function maskCardNumber(raw) {
    const digits = raw.replace(/\D/g, "").slice(0, 16);
    const padded = digits.padEnd(16, "0");
    return `**** **** **** ${padded.slice(12)}`;
}

export function Cart() {
    const navigate = useNavigate();
    const { items, removeFromCart, clearCart } = useCart();
    const { addPurchase } = usePurchases();

    const [cities, setCities] = useState([]);

    useEffect(() => {
        async function loadCities() {
            try {
                const data = await getCities();
                setCities(data);
            } catch (error) {
                console.error("Error loading cities:", error);
            }
        }
        loadCities();
    }, []);

    const [step, setStep] = useState("idle");
    const [shipping, setShipping] = useState(emptyShipping);
    const [payment, setPayment] = useState(emptyPayment);
    const [shippingErrors, setShippingErrors] = useState({});
    const [paymentErrors, setPaymentErrors] = useState({});
    const [confirmedTotal, setConfirmedTotal] = useState(0);

    const total = items.reduce((sum, item) => sum + item.price, 0);

    function findCityId(cityName) {
        const match = cities.find(
            c => c.cityName.trim().toLowerCase() === cityName.trim().toLowerCase()
        );
        return match ? match.cityId : null;
    }

    function validateShipping() {
        const errors = {};
        if (!shipping.street.trim()) errors.street = "Street is required";
        if (!shipping.number.trim()) errors.number = "Number is required";
        if (!shipping.zipCode.trim()) errors.zipCode = "Zip code is required";
        if (!shipping.city.trim()) {
            errors.city = "City is required";
        } else if (!findCityId(shipping.city)) {
            errors.city = "City not recognized. Please check the spelling.";
        }
        setShippingErrors(errors);
        return Object.keys(errors).length === 0;
    }

    function validatePayment() {
        const errors = {};
        if (!payment.cardHolderName.trim()) errors.cardHolderName = "Card holder name is required";
        if (payment.cardNumber.replace(/\D/g, "").length < 16) errors.cardNumber = "Enter a valid 16-digit card number";
        if (!payment.expiryMonth.trim() || !/^\d{2}$/.test(payment.expiryMonth) || +payment.expiryMonth < 1 || +payment.expiryMonth > 12)
            errors.expiryMonth = "Enter a valid month (01–12)";
        if (!payment.expiryYear.trim() || !/^\d{4}$/.test(payment.expiryYear))
            errors.expiryYear = "Enter a valid 4-digit year";
        setPaymentErrors(errors);
        return Object.keys(errors).length === 0;
    }

    function handleShippingNext() {
        if (validateShipping()) setStep("payment");
    }

    async function handlePaymentConfirm() {
        if (!validatePayment()) return;

        try {
            const payload = {
                buyerId: CURRENT_USER_ID,
                price: total.toFixed(2),
                transactionDate: new Date().toISOString().slice(0, 19),
                street: shipping.street,
                number: shipping.number,
                zipCode: shipping.zipCode,
                cityId: findCityId(shipping.city),
                cardHolderName: payment.cardHolderName,
                maskedCardNumber: maskCardNumber(payment.cardNumber),
                expiryMonth: payment.expiryMonth,
                expiryYear: payment.expiryYear,
                listingIds: items.map(item => item.id),
            };

            await createTransaction(payload);

            for (const item of items) {
                try {
                    await markListingAsSold(item.id);
                } catch (err) {
                    console.error(`Failed to mark listing ${item.id} as sold:`, err);
                }
            }

            setConfirmedTotal(total);
            clearCart();
            setStep("success");
        } catch (e) {
            console.error("[Cart] transaction failed:", e);
            alert("Purchase failed. Check backend console.");
        }
    }

    function formatCardDisplay(value) {
        return value.replace(/\D/g, "").slice(0, 16).replace(/(.{4})/g, "$1 ").trim();
    }

    const inputClass = (error) =>
        `w-full px-4 py-3 border-2 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary transition-colors ${
            error ? "border-destructive focus:ring-destructive" : "border-border focus:border-primary"
        }`;

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
                        <ShoppingCart className="w-10 h-10 text-primary" />
                        Shopping Cart
                    </h1>
                    <p className="text-muted-foreground">
                        {items.length} {items.length === 1 ? "item" : "items"} in cart
                    </p>
                </div>

                {items.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <ShoppingCart className="w-16 h-16 mx-auto mb-4 text-muted-foreground opacity-50" />
                        <h2 className="font-serif text-2xl mb-2 text-foreground">Your Cart is Empty</h2>
                        <p className="text-muted-foreground mb-6">Add items to your cart to continue</p>
                        <button
                            onClick={() => navigate("/explore")}
                            className="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                        >
                            Browse Marketplace
                        </button>
                    </div>
                ) : (
                    <div className="space-y-6">
                        {/* Cart Items */}
                        <div className="bg-white border-2 border-border rounded-2xl p-6 shadow-sm">
                            <h2 className="font-serif text-2xl mb-6 text-foreground">Items</h2>
                            <div className="space-y-4">
                                {items.map((item) => (
                                    <div
                                        key={item.id}
                                        className="flex items-center justify-between p-4 border-2 border-border rounded-xl hover:border-primary/50 transition-colors"
                                    >
                                        <div className="flex-1">
                                            <h3 className="font-semibold text-lg mb-1">{item.title}</h3>
                                            <p className="text-sm text-muted-foreground">€{item.price.toFixed(2)}</p>
                                        </div>
                                        <button
                                            onClick={() => removeFromCart(item.id)}
                                            className="p-2 text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                                            title="Remove"
                                        >
                                            <Trash2 className="w-5 h-5" />
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* Order Summary */}
                        <div className="bg-white border-2 border-border rounded-2xl p-6 shadow-sm">
                            <h2 className="font-serif text-2xl mb-6 text-foreground">Order Summary</h2>
                            <div className="space-y-3 mb-6">
                                <div className="flex justify-between text-muted-foreground">
                                    <span>Subtotal</span>
                                    <span>€{total.toFixed(2)}</span>
                                </div>
                                <div className="flex justify-between text-muted-foreground">
                                    <span>Shipping</span>
                                    <span>To be calculated</span>
                                </div>
                                <div className="border-t-2 border-border pt-3 flex justify-between text-foreground">
                                    <span className="font-bold text-xl">Total</span>
                                    <span className="font-bold text-xl text-primary">€{total.toFixed(2)}</span>
                                </div>
                            </div>

                            <button
                                onClick={() => setStep("shipping")}
                                className="w-full px-8 py-4 bg-gradient-to-r from-primary to-accent text-white rounded-xl hover:shadow-2xl hover:scale-105 transition-all font-semibold text-lg"
                            >
                                Proceed to Checkout
                            </button>

                            <button
                                onClick={() => navigate("/explore")}
                                className="w-full mt-3 px-8 py-4 bg-white border-2 border-border text-foreground rounded-xl hover:bg-muted transition-colors font-semibold"
                            >
                                Continue Shopping
                            </button>
                        </div>
                    </div>
                )}
            </div>

            {/* ── Checkout Modal ── */}
            {step !== "idle" && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl max-w-lg w-full shadow-2xl overflow-hidden">

                        {/* Progress bar */}
                        {step !== "success" && (
                            <div className="flex border-b-2 border-border">
                                {["shipping", "payment"].map((s, i) => (
                                    <div
                                        key={s}
                                        className={`flex-1 py-3 text-center text-sm font-semibold flex items-center justify-center gap-2 transition-colors ${
                                            step === s
                                                ? "bg-primary/10 text-primary border-b-2 border-primary"
                                                : i === 0 && step === "payment"
                                                    ? "text-muted-foreground"
                                                    : "text-muted-foreground"
                                        }`}
                                    >
                                        {s === "shipping" ? <MapPin className="w-4 h-4" /> : <CreditCard className="w-4 h-4" />}
                                        {s === "shipping" ? "Shipping Address" : "Payment Details"}
                                    </div>
                                ))}
                            </div>
                        )}

                        <div className="p-8">

                            {/* ── Step 1: Shipping ── */}
                            {step === "shipping" && (
                                <>
                                    <h2 className="font-serif text-2xl mb-1 text-foreground flex items-center gap-2">
                                        <MapPin className="w-6 h-6 text-primary" /> Shipping Address
                                    </h2>
                                    <p className="text-muted-foreground text-sm mb-6">Enter the delivery address for your order.</p>

                                    <div className="space-y-4">
                                        <div className="grid grid-cols-3 gap-4">
                                            <div className="col-span-2">
                                                <label className="block text-sm font-semibold mb-1">Street</label>
                                                <input
                                                    type="text"
                                                    placeholder="e.g. Main Street"
                                                    value={shipping.street}
                                                    onChange={e => setShipping(s => ({ ...s, street: e.target.value }))}
                                                    className={inputClass(shippingErrors.street)}
                                                />
                                                {shippingErrors.street && <p className="text-destructive text-xs mt-1">{shippingErrors.street}</p>}
                                            </div>
                                            <div>
                                                <label className="block text-sm font-semibold mb-1">Number</label>
                                                <input
                                                    type="text"
                                                    placeholder="e.g. 42"
                                                    value={shipping.number}
                                                    onChange={e => setShipping(s => ({ ...s, number: e.target.value }))}
                                                    className={inputClass(shippingErrors.number)}
                                                />
                                                {shippingErrors.number && <p className="text-destructive text-xs mt-1">{shippingErrors.number}</p>}
                                            </div>
                                        </div>

                                        <div className="grid grid-cols-2 gap-4">
                                            <div>
                                                <label className="block text-sm font-semibold mb-1">Zip Code</label>
                                                <input
                                                    type="text"
                                                    placeholder="e.g. 1000-001"
                                                    value={shipping.zipCode}
                                                    onChange={e => setShipping(s => ({ ...s, zipCode: e.target.value }))}
                                                    className={inputClass(shippingErrors.zipCode)}
                                                />
                                                {shippingErrors.zipCode && <p className="text-destructive text-xs mt-1">{shippingErrors.zipCode}</p>}
                                            </div>
                                            <div>
                                                <label className="block text-sm font-semibold mb-1">City</label>
                                                <input
                                                    type="text"
                                                    placeholder="e.g. Lisbon"
                                                    value={shipping.city}
                                                    onChange={e => setShipping(s => ({ ...s, city: e.target.value }))}
                                                    className={inputClass(shippingErrors.city)}
                                                />
                                                {shippingErrors.city && <p className="text-destructive text-xs mt-1">{shippingErrors.city}</p>}
                                            </div>
                                        </div>
                                    </div>

                                    <div className="flex gap-3 mt-8">
                                        <button
                                            onClick={() => setStep("idle")}
                                            className="flex-1 px-6 py-3 border-2 border-border rounded-xl hover:bg-muted transition-colors font-semibold"
                                        >
                                            Cancel
                                        </button>
                                        <button
                                            onClick={handleShippingNext}
                                            className="flex-1 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                                        >
                                            Next: Payment
                                        </button>
                                    </div>
                                </>
                            )}

                            {/* ── Step 2: Payment ── */}
                            {step === "payment" && (
                                <>
                                    <h2 className="font-serif text-2xl mb-1 text-foreground flex items-center gap-2">
                                        <CreditCard className="w-6 h-6 text-primary" /> Payment Details
                                    </h2>
                                    <p className="text-muted-foreground text-sm mb-6">Enter your card information to complete the purchase.</p>

                                    <div className="space-y-4">
                                        <div>
                                            <label className="block text-sm font-semibold mb-1">Card Holder Name</label>
                                            <input
                                                type="text"
                                                placeholder="e.g. John Smith"
                                                value={payment.cardHolderName}
                                                onChange={e => setPayment(p => ({ ...p, cardHolderName: e.target.value }))}
                                                className={inputClass(paymentErrors.cardHolderName)}
                                            />
                                            {paymentErrors.cardHolderName && <p className="text-destructive text-xs mt-1">{paymentErrors.cardHolderName}</p>}
                                        </div>

                                        <div>
                                            <label className="block text-sm font-semibold mb-1">Card Number</label>
                                            <input
                                                type="text"
                                                placeholder="1234 5678 9012 3456"
                                                value={formatCardDisplay(payment.cardNumber)}
                                                onChange={e => setPayment(p => ({ ...p, cardNumber: e.target.value.replace(/\D/g, "").slice(0, 16) }))}
                                                className={inputClass(paymentErrors.cardNumber)}
                                                maxLength={19}
                                            />
                                            {paymentErrors.cardNumber && <p className="text-destructive text-xs mt-1">{paymentErrors.cardNumber}</p>}
                                        </div>

                                        <div className="grid grid-cols-2 gap-4">
                                            <div>
                                                <label className="block text-sm font-semibold mb-1">Expiry Month</label>
                                                <input
                                                    type="text"
                                                    placeholder="MM (e.g. 09)"
                                                    value={payment.expiryMonth}
                                                    onChange={e => setPayment(p => ({ ...p, expiryMonth: e.target.value.replace(/\D/g, "").slice(0, 2) }))}
                                                    className={inputClass(paymentErrors.expiryMonth)}
                                                    maxLength={2}
                                                />
                                                {paymentErrors.expiryMonth && <p className="text-destructive text-xs mt-1">{paymentErrors.expiryMonth}</p>}
                                            </div>
                                            <div>
                                                <label className="block text-sm font-semibold mb-1">Expiry Year</label>
                                                <input
                                                    type="text"
                                                    placeholder="YYYY (e.g. 2027)"
                                                    value={payment.expiryYear}
                                                    onChange={e => setPayment(p => ({ ...p, expiryYear: e.target.value.replace(/\D/g, "").slice(0, 4) }))}
                                                    className={inputClass(paymentErrors.expiryYear)}
                                                    maxLength={4}
                                                />
                                                {paymentErrors.expiryYear && <p className="text-destructive text-xs mt-1">{paymentErrors.expiryYear}</p>}
                                            </div>
                                        </div>
                                    </div>

                                    {/* Order total recap */}
                                    <div className="mt-6 p-4 bg-muted/40 rounded-xl border-2 border-border">
                                        <div className="flex justify-between font-bold text-foreground">
                                            <span>Order Total</span>
                                            <span className="text-primary">€{total.toFixed(2)}</span>
                                        </div>
                                    </div>

                                    <div className="flex gap-3 mt-6">
                                        <button
                                            onClick={() => setStep("shipping")}
                                            className="flex-1 px-6 py-3 border-2 border-border rounded-xl hover:bg-muted transition-colors font-semibold"
                                        >
                                            Back
                                        </button>
                                        <button
                                            onClick={handlePaymentConfirm}
                                            className="flex-1 px-6 py-3 bg-gradient-to-r from-primary to-accent text-white rounded-xl hover:shadow-xl transition-all font-semibold"
                                        >
                                            Confirm Purchase
                                        </button>
                                    </div>
                                </>
                            )}

                            {/* ── Step 3: Success ── */}
                            {step === "success" && (
                                <div className="text-center py-4">
                                    <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
                                        <CheckCircle className="w-12 h-12 text-green-500" />
                                    </div>

                                    <h2 className="font-serif text-3xl mb-3 text-foreground">Purchase Successful!</h2>
                                    <p className="text-muted-foreground mb-2">
                                        Your order has been placed successfully.
                                    </p>

                                    <div className="flex items-center justify-center gap-2 text-muted-foreground text-sm mb-6">
                                        <Mail className="w-4 h-4" />
                                        <span>A confirmation email will be sent to you shortly.</span>
                                    </div>

                                    {/* Order recap */}
                                    <div className="bg-muted/40 rounded-xl border-2 border-border p-4 text-left mb-6 space-y-2 text-sm">
                                        <div className="flex justify-between text-muted-foreground">
                                            <span>Shipping to</span>
                                            <span className="text-foreground font-medium">{shipping.street}, no. {shipping.number}, {shipping.zipCode}, {shipping.city}</span>
                                        </div>
                                        <div className="flex justify-between text-muted-foreground">
                                            <span>Card</span>
                                            <span className="text-foreground font-medium">{maskCardNumber(payment.cardNumber)}</span>
                                        </div>
                                        <div className="flex justify-between font-bold text-foreground border-t-2 border-border pt-2 mt-2">
                                            <span>Total charged</span>
                                            <span className="text-primary">€{confirmedTotal.toFixed(2)}</span>
                                        </div>
                                    </div>

                                    <button
                                        onClick={() => {
                                            setStep("idle");
                                            setShipping(emptyShipping);
                                            setPayment(emptyPayment);
                                            navigate("/my-purchases");
                                        }}
                                        className="w-full px-8 py-4 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                                    >
                                        View My Purchases
                                    </button>
                                </div>
                            )}

                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
