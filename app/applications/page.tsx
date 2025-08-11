
'use client'
import { useState, useEffect } from 'react'
import { applicationApi } from '../lib/api'
import { ApplicationDto } from '../types'
import { Plus, Search, Filter } from 'lucide-react'
import toast from 'react-hot-toast'
import ApplicationForm from './components/ApplicationForm'
import ApplicationCard from './components/ApplicationCard'

export default function Applications() {
  const [applications, setApplications] = useState<ApplicationDto[]>([])
  const [filteredApplications, setFilteredApplications] = useState<ApplicationDto[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [streamFilter, setStreamFilter] = useState('')
  const [crewFilter, setCrewFilter] = useState('')

  useEffect(() => {
    loadApplications()
  }, [])

  useEffect(() => {
    filterApplications()
  }, [applications, searchTerm, streamFilter, crewFilter])

  const loadApplications = async () => {
    try {
      setLoading(true)
      const response = await applicationApi.getAll()
      setApplications(response.applications)
    } catch (error) {
      toast.error('Failed to load applications')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const filterApplications = () => {
    let filtered = applications

    if (searchTerm) {
      filtered = filtered.filter(app =>
        app.applicationName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        app.applicationId.toLowerCase().includes(searchTerm.toLowerCase()) ||
        app.applicationDescription.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    if (streamFilter) {
      filtered = filtered.filter(app => app.stream === streamFilter)
    }

    if (crewFilter) {
      filtered = filtered.filter(app => app.crew === crewFilter)
    }

    setFilteredApplications(filtered)
  }

  const handleApplicationCreated = (newApp: ApplicationDto) => {
    setApplications(prev => [...prev, newApp])
    setShowForm(false)
    toast.success('Application registered successfully')
  }

  const uniqueStreams = [...new Set(applications.map(app => app.stream))]
  const uniqueCrews = [...new Set(applications.map(app => app.crew))]

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Applications</h1>
        <button
          onClick={() => setShowForm(true)}
          className="btn-primary flex items-center space-x-2"
        >
          <Plus size={18} />
          <span>Register Application</span>
        </button>
      </div>

      {/* Filters */}
      <div className="card p-4">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="relative">
            <Search className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
            <input
              type="text"
              placeholder="Search applications..."
              className="pl-10 pr-4 py-2 w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <select
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={streamFilter}
            onChange={(e) => setStreamFilter(e.target.value)}
          >
            <option value="">All Streams</option>
            {uniqueStreams.map(stream => (
              <option key={stream} value={stream}>{stream}</option>
            ))}
          </select>

          <select
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={crewFilter}
            onChange={(e) => setCrewFilter(e.target.value)}
          >
            <option value="">All Crews</option>
            {uniqueCrews.map(crew => (
              <option key={crew} value={crew}>{crew}</option>
            ))}
          </select>

          <button
            onClick={() => {
              setSearchTerm('')
              setStreamFilter('')
              setCrewFilter('')
            }}
            className="btn-secondary flex items-center justify-center space-x-2"
          >
            <Filter size={18} />
            <span>Clear Filters</span>
          </button>
        </div>
      </div>

      {/* Applications Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredApplications.map((application) => (
          <ApplicationCard key={application.id} application={application} />
        ))}
      </div>

      {filteredApplications.length === 0 && !loading && (
        <div className="text-center py-12">
          <p className="text-gray-500">No applications found matching your criteria.</p>
        </div>
      )}

      {/* Application Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-md w-full">
            <ApplicationForm
              onSubmit={handleApplicationCreated}
              onCancel={() => setShowForm(false)}
            />
          </div>
        </div>
      )}
    </div>
  )
}
