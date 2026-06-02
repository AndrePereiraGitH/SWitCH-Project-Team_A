import { Link } from "react-router-dom";
import { BookOpen, Plus, List, ShoppingBag, LayoutGrid, Home } from "lucide-react";
import { usePublications } from "../components/PublicationsContext";
import { useLists } from "../components/ListsContext";
import { useLibrary } from "../components/LibraryContext";
import { useAuth } from "../components/AuthContext";
import { useEffect } from "react";

export function Dashboard() {
  const { getPublicationsForSale } = usePublications();
  const { lists } = useLists();
  const { library } = useLibrary();
  const { login } = useAuth();

  // Automatically log in when accessing dashboard
  useEffect(() => {
    login();
  }, [login]);

  const stats = {
    booksInLibrary: library ? library.publicationIds.length : 0,
    activeLists: lists.length,
    itemsForSale: getPublicationsForSale().length
  };

  return (
    <div className="min-h-screen bg-background pt-20 pb-12">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <Link
          to="/"
          className="inline-flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
        >
          <Home className="w-4 h-4" />
          Home
        </Link>

        <div className="mb-8">
          <h1 className="font-serif text-4xl mb-2 text-foreground">My Dashboard</h1>
          <p className="text-muted-foreground">Manage your library and sales</p>
        </div>

        {/* Stats Grid */}
        <div className="grid md:grid-cols-3 gap-6 mb-12">
          <div className="bg-card border border-border rounded-lg p-6">
            <div className="flex items-center justify-between mb-2">
              <span className="text-muted-foreground">Books in Library</span>
              <BookOpen className="w-5 h-5 text-primary" />
            </div>
            <p className="text-3xl font-bold text-primary">{stats.booksInLibrary}</p>
          </div>

          <div className="bg-card border border-border rounded-lg p-6">
            <div className="flex items-center justify-between mb-2">
              <span className="text-muted-foreground">Active Lists</span>
              <List className="w-5 h-5 text-accent" />
            </div>
            <p className="text-3xl font-bold text-accent">{stats.activeLists}</p>
          </div>

          <div className="bg-card border border-border rounded-lg p-6">
            <div className="flex items-center justify-between mb-2">
              <span className="text-muted-foreground">Items For Sale</span>
              <ShoppingBag className="w-5 h-5 text-secondary" />
            </div>
            <p className="text-3xl font-bold text-secondary">{stats.itemsForSale}</p>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="mb-12">
          <h2 className="font-serif text-2xl mb-6">Quick Actions</h2>
          <div className="grid md:grid-cols-2 lg:grid-cols-5 gap-4">
            <Link
              to="/add-publication"
              className="flex flex-col items-center justify-center p-6 bg-card border-2 border-dashed border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-colors group"
            >
              <Plus className="w-8 h-8 text-primary mb-2 group-hover:scale-110 transition-transform" />
              <span className="font-semibold text-center">Add Publication</span>
            </Link>

            <Link
              to="/my-publications"
              className="flex flex-col items-center justify-center p-6 bg-card border-2 border-dashed border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-colors group"
            >
              <BookOpen className="w-8 h-8 text-primary mb-2 group-hover:scale-110 transition-transform" />
              <span className="font-semibold text-center">My Publications</span>
            </Link>

            <Link
              to="/library"
              className="flex flex-col items-center justify-center p-6 bg-card border-2 border-dashed border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-colors group"
            >
              <LayoutGrid className="w-8 h-8 text-primary mb-2 group-hover:scale-110 transition-transform" />
              <span className="font-semibold text-center">View Library</span>
            </Link>

            <Link
              to="/lists"
              className="flex flex-col items-center justify-center p-6 bg-card border-2 border-dashed border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-colors group"
            >
              <List className="w-8 h-8 text-primary mb-2 group-hover:scale-110 transition-transform" />
              <span className="font-semibold text-center">Manage Lists</span>
            </Link>

            <Link
              to="/explore"
              className="flex flex-col items-center justify-center p-6 bg-card border-2 border-dashed border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-colors group"
            >
              <ShoppingBag className="w-8 h-8 text-primary mb-2 group-hover:scale-110 transition-transform" />
              <span className="font-semibold text-center">Explore Sales</span>
            </Link>
          </div>
        </div>

        {/* Recent Activity */}
        <div>
          <h2 className="font-serif text-2xl mb-6">Recent Activity</h2>
          <div className="bg-card border border-border rounded-lg p-8 text-center text-muted-foreground">
            <BookOpen className="w-12 h-12 mx-auto mb-4 opacity-50" />
            <p>Start by adding publications to your library</p>
          </div>
        </div>
      </div>
    </div>
  );
}
