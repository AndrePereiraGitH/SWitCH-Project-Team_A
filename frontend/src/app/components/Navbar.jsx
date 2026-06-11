import { Link, useLocation, useNavigate } from "react-router-dom";
import { User, Search, ShoppingCart, LogOut, BookOpen, Library, List } from "lucide-react";
import { useCart } from "./CartContext";
import { useAuth } from "./AuthContext";
import logoImg from "../../assets/teste.png";
import { useState, useEffect, useRef } from "react";

export function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { itemCount } = useCart();
  const { isLoggedIn, logout } = useAuth();
  const [searchQuery, setSearchQuery] = useState("");
  const [showUserDropdown, setShowUserDropdown] = useState(false);
  const dropdownRef = useRef(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowUserDropdown(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/explore?search=${encodeURIComponent(searchQuery.trim())}`);
      setSearchQuery("");
    }
  };

  const handleLogout = () => {
    logout();
    setShowUserDropdown(false);
    navigate("/");
  };

  const isActive = (path) => {
    return location.pathname === path || location.pathname.startsWith(path);
  };

  const getLinkClass = (path) => {
    const base = "px-4 py-2 rounded-lg font-medium text-sm transition-all";
    if (isActive(path)) {
      return `${base} bg-primary text-white shadow-md`;
    }
    return `${base} text-foreground hover:bg-gray-100`;
  };

  return (
    <nav className="fixed top-0 w-full bg-white border-b border-border z-50 shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-20 gap-8">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-3 hover:opacity-80 transition-opacity flex-shrink-0">
            <img src={logoImg} alt="MiteLovers" className="w-10 h-10" />
            <span className="logo-text font-bold text-2xl">MiteLovers</span>
          </Link>

          {/* Search Bar */}
          <div className="hidden md:flex flex-1 max-w-md">
            <form onSubmit={handleSearch} className="relative w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Search by title, author, ISBN..."
                className="w-full pl-10 pr-4 py-2 bg-input-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent text-sm"
              />
            </form>
          </div>

          {/* Navigation Links */}
          <div className="hidden lg:flex items-center gap-2 flex-shrink-0">
            <Link
              to="/"
              className={getLinkClass("/about")}
            >
              About Us
            </Link>
            <Link to="/explore" className={getLinkClass("/explore")}>
              Marketplace
            </Link>
            <Link to="/shared-lists" className={getLinkClass("/shared-lists")}>
              Community
            </Link>
          </div>

          {/* Right Side Actions */}
          <div className="flex items-center gap-3 flex-shrink-0">
            {/* Shopping Cart */}
            <Link
              to="/cart"
              className="relative p-2 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <ShoppingCart className="w-5 h-5" />
              {itemCount > 0 && (
                <span className="absolute -top-1 -right-1 w-5 h-5 bg-gradient-to-br from-accent to-primary text-white text-xs font-bold rounded-full flex items-center justify-center shadow-lg">
                  {itemCount}
                </span>
              )}
            </Link>

            {/* User Login/Profile */}
            {isLoggedIn ? (
              <div className="relative" ref={dropdownRef}>
                <button
                  onClick={() => setShowUserDropdown(!showUserDropdown)}
                  className="flex items-center justify-center w-10 h-10 bg-gradient-to-br from-primary to-accent text-white rounded-full hover:shadow-lg transition-all font-semibold"
                >
                  U
                </button>

                {/* User Dropdown Menu */}
                {showUserDropdown && (
                  <div className="absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-2xl border border-border overflow-hidden">
                    <div className="p-3 bg-gradient-to-r from-primary to-accent text-white">
                      <p className="font-semibold">User Account</p>
                      <p className="text-xs opacity-90">user@example.com</p>
                    </div>
                    <div className="py-2">
                      <Link
                        to="/my-publications"
                        className="flex items-center gap-3 px-4 py-3 hover:bg-gray-50 transition-colors"
                        onClick={() => setShowUserDropdown(false)}
                      >
                        <BookOpen className="w-4 h-4 text-primary" />
                        <span className="text-sm font-medium">My Publications</span>
                      </Link>
                      <Link
                        to="/library"
                        className="flex items-center gap-3 px-4 py-3 hover:bg-gray-50 transition-colors"
                        onClick={() => setShowUserDropdown(false)}
                      >
                        <Library className="w-4 h-4 text-primary" />
                        <span className="text-sm font-medium">My Library</span>
                      </Link>
                      <Link
                        to="/lists"
                        className="flex items-center gap-3 px-4 py-3 hover:bg-gray-50 transition-colors"
                        onClick={() => setShowUserDropdown(false)}
                      >
                        <List className="w-4 h-4 text-primary" />
                        <span className="text-sm font-medium">My Lists</span>
                      </Link>
                      <div className="border-t border-border my-2"></div>
                      <button
                        onClick={handleLogout}
                        className="flex items-center gap-3 px-4 py-3 hover:bg-red-50 transition-colors w-full text-left"
                      >
                        <LogOut className="w-4 h-4 text-red-600" />
                        <span className="text-sm font-medium text-red-600">Logout</span>
                      </button>
                    </div>
                  </div>
                )}
              </div>
            ) : (
              <Link
                to="/dashboard"
                className="flex items-center gap-2 px-4 py-2 bg-gradient-to-r from-primary to-accent text-white rounded-lg hover:shadow-lg transition-all font-medium"
              >
                <User className="w-4 h-4" />
                <span className="hidden sm:inline">Login</span>
              </Link>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
