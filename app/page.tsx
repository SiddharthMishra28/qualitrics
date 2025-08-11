
'use client'
import { useState, useEffect, Fragment } from 'react'
import { dashboardApi, applicationApi } from './lib/api'
import { OverallExecutionSummary, ApplicationExecutionSummary, ApplicationDto, DailyExecutionSummary } from './types'
import { 
  BarChart3, TrendingUp, TrendingDown, Activity, Filter, Download, 
  PieChart, Eye, RefreshCw, Calendar, Search, ChevronDown, X
} from 'lucide-react'
import toast from 'react-hot-toast'
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from 'recharts';

export default function Dashboard() {
  const [overallSummary, setOverallSummary] = useState<OverallExecutionSummary | null>(null)
  const [functionalSummary, setFunctionalSummary] = useState<OverallExecutionSummary | null>(null)
  const [regressionSummary, setRegressionSummary] = useState<OverallExecutionSummary | null>(null)
  const [sanitySummary, setSanitySummary] = useState<OverallExecutionSummary | null>(null)
  const [smokeSummary, setSmokeSummary] = useState<OverallExecutionSummary | null>(null)
  const [appSummaries, setAppSummaries] = useState<ApplicationExecutionSummary[]>([])
  const [applications, setApplications] = useState<ApplicationDto[]>([])
  const [selectedApp, setSelectedApp] = useState('')
  const [appSummary, setAppSummary] = useState<OverallExecutionSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [selectedView, setSelectedView] = useState<'overview' | 'execution-types' | 'suite-categories' | 'applications' | 'trends'>('overview')
  const [tableData, setTableData] = useState<any[]>([])
  const [showTable, setShowTable] = useState(false)
  const [tableTitle, setTableTitle] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [dateFilter, setDateFilter] = useState('all')
  const [statusFilter, setStatusFilter] = useState('all')
  const [overallDailyTrends, setOverallDailyTrends] = useState<DailyExecutionSummary[]>([])
  const [applicationDailyTrends, setApplicationDailyTrends] = useState<DailyExecutionSummary[]>([])
  const [trendDateRange, setTrendDateRange] = useState('all') // 'all', '7days', '30days'
  const [selectedStream, setSelectedStream] = useState('')
  const [selectedCrew, setSelectedCrew] = useState('')
  const [distinctStreams, setDistinctStreams] = useState<string[]>([])
  const [distinctCrews, setDistinctCrews] = useState<string[]>([])
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = date.toLocaleString('default', { month: 'short' }).toUpperCase();
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
  };

  useEffect(() => {
    loadDashboardData()
    const fetchDistinctFilters = async () => {
      try {
        const streams = await applicationApi.getDistinctStreams()
        setDistinctStreams(streams)
        const crews = await applicationApi.getDistinctCrews()
        setDistinctCrews(crews)
      } catch (error) {
        toast.error('Failed to load filter options')
        console.error(error)
      }
    }
    fetchDistinctFilters()
  }, [])

  useEffect(() => {
    if (selectedApp) {
      loadApplicationAnalytics()
    } else {
      setAppSummary(null)
    }
  }, [selectedApp])

  const loadDashboardData = async () => {
    try {
      setLoading(true)
      const [overall, apps, applications, functional, regression, sanity, smoke, overallTrends] = await Promise.all([
        dashboardApi.getOverallSummary(selectedApp, selectedStream, selectedCrew),
        dashboardApi.getAllApplicationsSummary(selectedStream, selectedCrew),
        applicationApi.getAll(), // This doesn't need filters as it's for the dropdown
        dashboardApi.getExecutionSummary('functional', selectedApp, selectedStream, selectedCrew),
        dashboardApi.getExecutionSummary('regression', selectedApp, selectedStream, selectedCrew),
        dashboardApi.getSuiteSummary('sanity', selectedApp, selectedStream, selectedCrew),
        dashboardApi.getSuiteSummary('smoke', selectedApp, selectedStream, selectedCrew),
        dashboardApi.getOverallDailyExecutionTrends(selectedApp, selectedStream, selectedCrew) // Fetch overall daily trends
      ])
      setOverallSummary(overall)
      setAppSummaries(apps)
      setApplications(applications.applications)
      setFunctionalSummary(functional)
      setRegressionSummary(regression)
      setSanitySummary(sanity)
      setSmokeSummary(smoke)
      setOverallDailyTrends(overallTrends) // Set overall daily trends
    } catch (error) {
      toast.error('Failed to load dashboard data')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const loadApplicationAnalytics = async () => {
    if (!selectedApp) return
    
    try {
      const [summary, appTrends] = await Promise.all([
        dashboardApi.getApplicationSummary(selectedApp),
        dashboardApi.getApplicationDailyExecutionTrends(selectedApp, selectedStream, selectedCrew) // Fetch application daily trends with filters
      ])
      setAppSummary(summary)
      setApplicationDailyTrends(appTrends) // Set application daily trends
    } catch (error) {
      toast.error('Failed to load application analytics')
      console.error(error)
    }
  }

  const handleChartClick = async (type: string, data: any) => {
    if (type === 'application') {
      try {
        setLoading(true);
        const response = await applicationApi.getExecutions(data.applicationId);
        setTableData(response.executions);
        setTableTitle(`Executions for ${data.applicationName}`);
        setShowTable(true);
      } catch (error) {
        toast.error('Failed to load execution data');
        console.error(error);
      } finally {
        setLoading(false);
      }
      return;
    }

    let filteredData: any[] = []
    
    switch(type) {
      case 'overall':
        filteredData = [{
          category: 'Overall Summary',
          totalBuilds: data.totalBuilds,
          passed: data.totalBuildsPassed,
          failed: data.totalBuildsFailed,
          successRate: `${data.percentPassed.toFixed(1)}%`
        }]
        setTableTitle('Overall Execution Summary')
        break
      case 'execution-distribution':
        filteredData = [
          { type: 'Functional', ...functionalSummary, successRate: `${functionalSummary?.percentPassed.toFixed(1)}%` },
          { type: 'Regression', ...regressionSummary, successRate: `${regressionSummary?.percentPassed.toFixed(1)}%` },
          { category: 'Sanity', ...sanitySummary, successRate: `${sanitySummary?.percentPassed.toFixed(1)}%` },
          { category: 'Smoke', ...smokeSummary, successRate: `${smokeSummary?.percentPassed.toFixed(1)}%` }
        ].filter(item => item.totalBuilds)
        setTableTitle('Execution Distribution Summary')
        break
      case 'applications':
        setLoading(true);
        const toastId = toast.loading('Loading all executions... This may take a moment.');
        try {
          const allExecutions = [];
          for (const app of applications) {
            try {
              const response = await applicationApi.getExecutions(app.applicationId);
              if (response.executions && response.executions.length > 0) {
                const executionsWithAppInfo = response.executions.map((exec: any) => ({
                  applicationName: app.applicationName,
                  applicationId: app.applicationId,
                  ...exec,
                }));
                allExecutions.push(...executionsWithAppInfo);
              }
            } catch (error) {
              console.error(`Failed to fetch executions for ${app.applicationName}`, error);
              // Silently fail for single app fetch, or show a small toast
            }
          }
          setTableData(allExecutions);
          setTableTitle('All Application Executions');
          setShowTable(true);
          toast.success('Successfully loaded all executions.', { id: toastId });
        } catch (error) {
          toast.error('Failed to load executions.', { id: toastId });
          console.error(error);
        } finally {
          setLoading(false);
        }
        return;
    }
    
    // Apply filters
    if (searchTerm) {
      filteredData = filteredData.filter(item => 
        Object.values(item).some(value => 
          value?.toString().toLowerCase().includes(searchTerm.toLowerCase())
        )
      )
    }
    
    if (statusFilter !== 'all') {
      filteredData = filteredData.filter(item => {
        const successRate = parseFloat(item.successRate?.replace('%', '') || '0')
        if (statusFilter === 'passed') return successRate >= 80
        if (statusFilter === 'warning') return successRate >= 60 && successRate < 80
        if (statusFilter === 'failed') return successRate < 60
        return true
      })
    }
    
    setTableData(filteredData)
    setShowTable(true)
  }

  const exportData = async (format: 'csv' | 'json' = 'csv') => {
    const toastId = toast.loading('Exporting all executions... This might take a while.');
    setLoading(true);

    try {
      const allExecutionsWithDetails = [];

      for (const app of applications) {
        try {
          const response = await applicationApi.getExecutions(app.applicationId);
          if (response.executions && response.executions.length > 0) {
            const executionsWithAppInfo = response.executions.map((execution: any) => ({
              applicationName: app.applicationName,
              applicationId: app.applicationId,
              ...execution,
            }));
            allExecutionsWithDetails.push(...executionsWithAppInfo);
          }
        } catch (error) {
          console.error(`Could not fetch executions for application ${app.applicationId}:`, error);
          toast.error(`Could not fetch executions for ${app.applicationName}.`, { id: toastId });
        }
      }

      if (allExecutionsWithDetails.length === 0) {
        toast.error("No executions found to export.", { id: toastId });
        return;
      }

      if (format === 'csv') {
        const replacer = (key: any, value: any) => value === null ? '' : value;
        const header = Object.keys(allExecutionsWithDetails[0]);
        const csv = allExecutionsWithDetails.map((row: any) =>
          header
            .map((fieldName) => JSON.stringify(row[fieldName], replacer))
            .join(',')
        );
        csv.unshift(header.join(','));
        const csvContent = csv.join('\r\n');

        const encodedUri = "data:text/csv;charset=utf-8," + encodeURIComponent(csvContent);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", `global-executions-export-${new Date().toISOString().split('T')[0]}.csv`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else {
        const jsonContent = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(allExecutionsWithDetails, null, 2));
        const link = document.createElement("a");
        link.setAttribute("href", jsonContent);
        link.setAttribute("download", `global-executions-export-${new Date().toISOString().split('T')[0]}.json`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }

      toast.success(`Successfully exported all executions as ${format.toUpperCase()}.`, { id: toastId });

    } catch (error) {
      console.error('Failed to export data:', error);
      toast.error('An unexpected error occurred during export.', { id: toastId });
    } finally {
      setLoading(false);
    }
  };

  const PieChart = ({ data, title, color, onClick }: { data: OverallExecutionSummary | null, title: string, color: string, onClick: () => void }) => {
    if (!data) return <div className="text-center text-gray-500">No data available</div>
    
    const radius = 70
    const circumference = 2 * Math.PI * radius
    const passedPercentage = data.percentPassed
    const strokeDasharray = `${(passedPercentage / 100) * circumference} ${circumference}`

    return (
      <div className="card p-6 cursor-pointer hover:shadow-lg transition-all duration-300 transform hover:scale-105" onClick={onClick}>
        <h3 className="text-lg font-semibold text-gray-900 mb-4 text-center">{title}</h3>
        <div className="flex items-center justify-center mb-4">
          <div className="relative">
            <svg width="160" height="160" className="transform -rotate-90">
              <circle
                cx="80"
                cy="80"
                r={radius}
                stroke="#e5e7eb"
                strokeWidth="12"
                fill="none"
              />
              <circle
                cx="80"
                cy="80"
                r={radius}
                stroke={color}
                strokeWidth="12"
                fill="none"
                strokeDasharray={strokeDasharray}
                strokeLinecap="round"
                className="transition-all duration-1000"
              />
            </svg>
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="text-center">
                <div className="text-3xl font-bold text-gray-900">{passedPercentage.toFixed(1)}%</div>
                <div className="text-sm text-gray-500">Success</div>
              </div>
            </div>
          </div>
        </div>
        <div className="grid grid-cols-3 gap-2 text-center">
          <div className="p-2 bg-gray-50 rounded">
            <div className="text-lg font-semibold">{data.totalBuilds}</div>
            <div className="text-xs text-gray-500">Total</div>
          </div>
          <div className="p-2 bg-green-50 rounded">
            <div className="text-lg font-semibold text-green-600">{data.totalBuildsPassed}</div>
            <div className="text-xs text-gray-500">Passed</div>
          </div>
          <div className="p-2 bg-red-50 rounded">
            <div className="text-lg font-semibold text-red-600">{data.totalBuildsFailed}</div>
            <div className="text-xs text-gray-500">Failed</div>
          </div>
        </div>
      </div>
    )
  }

  const TrendChart = ({ data, title }: { data: any[], title: string }) => {
    if (!data || data.length === 0) return <div className="text-center text-gray-500">No trend data available</div>

    // Prepare data for Recharts
    // Note: For date-wise trends, the 'data' prop would need to contain a date field
    // and the backend API would need to provide historical data.
    const chartData = data.map(item => ({
      name: item.name || item.applicationName || 'N/A', // 'name' can represent a time period or application name
      totalBuilds: item.totalBuilds || 0,
      passed: item.totalBuildsPassed || 0,
      failed: item.totalBuildsFailed || 0,
    }));

    const isDateAxis = title.includes('(Daily)');

    return (
      <div className="card p-6">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">{title}</h3>
        <div style={{ width: '100%', height: 300 }}>
          <ResponsiveContainer>
            <BarChart
              data={chartData}
              margin={{ top: 5, right: 30, left: 20, bottom: isDateAxis ? 5 : 75 }}
            >
              {isDateAxis ? (
                <XAxis dataKey="name" tickFormatter={(tick) => new Date(tick).toLocaleDateString()} />
              ) : (
                <XAxis dataKey="name" angle={-90} textAnchor="end" interval={0} />
              )}
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="totalBuilds" fill="#8884d8" name="Total Builds" />
              <Bar dataKey="passed" fill="#82ca9d" name="Passed" />
              <Bar dataKey="failed" fill="#ffc658" name="Failed" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    );
  }

  const DataTable = ({ data, title, onClose }: { data: any[], title: string, onClose: () => void }) => {
    const [currentPage, setCurrentPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const itemsPerPage = 10;

    const filteredData = data.filter(row =>
        Object.values(row).some(value =>
            value?.toString().toLowerCase().includes(searchTerm.toLowerCase())
        )
    );

    const totalPages = Math.ceil(filteredData.length / itemsPerPage);
    const paginatedData = filteredData.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

    const handlePrevPage = () => {
        setCurrentPage(prev => Math.max(prev - 1, 1));
    };

    const handleNextPage = () => {
        setCurrentPage(prev => Math.min(prev + 1, totalPages));
    };

    useEffect(() => {
        setCurrentPage(1);
    }, [data, searchTerm]);

    return (
      <Fragment>
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
        <div className="bg-white rounded-lg max-w-7xl w-full max-h-[90vh] overflow-hidden flex flex-col">
        <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center bg-gray-50">
          <h2 className="text-xl font-semibold text-gray-900">{title}</h2>
          <div className="flex items-center space-x-3">
            <div className="flex items-center space-x-2">
              <Search size={16} className="text-gray-400" />
              <input
                type="text"
                placeholder="Search..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="px-3 py-1 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <button onClick={onClose} className="p-2 text-gray-400 hover:text-gray-600">
              <X size={20} />
            </button>
          </div>
        </div>
        
        <div className="overflow-x-auto flex-grow">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50 sticky top-0">
              <tr>
                {paginatedData.length > 0 && Object.keys(paginatedData[0]).map(key => (
                  <th key={key} className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    {key.replace(/([A-Z])/g, ' $1').trim()}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {paginatedData.map((row, index) => (
                <tr key={index} className="hover:bg-gray-50 transition-colors">
                  {Object.keys(row).map((key, cellIndex) => (
                    <td key={cellIndex} className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {key === 'reportLink' ? (
                        <a href={row[key]} target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:underline">
                          View Report
                        </a>
                      ) : key === 'createdAt' ? (
                        formatDate(row[key])
                      ) : (row as any)[key]}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {totalPages > 1 && (
            <div className="px-6 py-4 border-t border-gray-200 flex justify-end items-center">
                <span className="text-sm text-gray-700 mr-4">
                    Page {currentPage} of {totalPages}
                </span>
                <div className="flex items-center space-x-2">
                    <button onClick={handlePrevPage} disabled={currentPage === 1}>Previous</button>
                    <button onClick={handleNextPage} disabled={currentPage === totalPages}>Next</button>
                </div>
            </div>
        )}
      </div>
    </div>
    </Fragment>
    )
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  const filterTrendData = (data: DailyExecutionSummary[], range: string) => {
    if (!data || data.length === 0) return [];
    const now = new Date();
    let startDate: Date;

    switch (range) {
      case '7days':
        startDate = new Date(now.setDate(now.getDate() - 7));
        break;
      case '30days':
        startDate = new Date(now.setDate(now.getDate() - 30));
        break;
      case 'all':
      default:
        return data.map(item => ({
          ...item,
          name: item.date // Use date for X-axis
        }));
    }

    return data.filter(item => new Date(item.date) >= startDate).map(item => ({
      ...item,
      name: item.date // Use date for X-axis
    }));
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Test Management Dashboard</h1>
        <div className="flex items-center space-x-3">
          <button
            onClick={loadDashboardData}
            className="btn-primary flex items-center space-x-2"
          >
            <RefreshCw size={18} />
            <span>Refresh</span>
          </button>
          <button onClick={() => exportData('csv')} className="btn-secondary flex items-center space-x-2">
            <Download size={18} />
            <span>Export Data</span>
          </button>
        </div>
      </div>

      {/* Global Filters */}
      <div className="card p-4 flex flex-wrap gap-4 items-center">
        <h3 className="text-lg font-semibold text-gray-900">Filter Dashboard:</h3>
        <select
          value={selectedApp}
          onChange={(e) => setSelectedApp(e.target.value)}
          className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="">All Applications</option>
          {applications.map((app) => (
            <option key={app.applicationId} value={app.applicationId}>
              {app.applicationName}
            </option>
          ))}
        </select>

        <select
          value={selectedStream}
          onChange={(e) => setSelectedStream(e.target.value)}
          className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="">All Streams</option>
          {distinctStreams.map((stream) => (
            <option key={stream} value={stream}>
              {stream}
            </option>
          ))}
        </select>

        <select
          value={selectedCrew}
          onChange={(e) => setSelectedCrew(e.target.value)}
          className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="">All Crews</option>
          {distinctCrews.map((crew) => (
            <option key={crew} value={crew}>
              {crew}
            </option>
          ))}
        </select>
        <button onClick={loadDashboardData} className="btn-primary flex items-center space-x-2">
          <Filter size={18} />
          <span>Apply Filters</span>
        </button>
      </div>

      {/* Filter Tabs */}
      <div className="flex space-x-1 bg-gray-100 p-1 rounded-lg">
        {[
          { key: 'overview', label: 'Overview', icon: Activity },
          { key: 'execution-distribution', label: 'Execution Distribution', icon: PieChart },
          { key: 'applications', label: 'Applications', icon: Filter },
          { key: 'trends', label: 'Trends', icon: TrendingUp }
        ].map(({ key, label, icon: Icon }) => (
          <button
            key={key}
            onClick={() => setSelectedView(key as any)}
            className={`flex items-center space-x-2 px-4 py-2 rounded-md font-medium transition-colors ${
              selectedView === key 
                ? 'bg-white text-blue-600 shadow-sm' 
                : 'text-gray-600 hover:text-gray-800'
            }`}
          >
            <Icon size={18} />
            <span>{label}</span>
          </button>
        ))}
      </div>

      {/* Overview Dashboard */}
      {selectedView === 'overview' && overallSummary && (
        <>
          {/* KPI Cards */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            <div className="card p-6 cursor-pointer hover:shadow-lg transition-shadow bg-gradient-to-r from-blue-50 to-blue-100" 
                 onClick={() => handleChartClick('overall', overallSummary)}>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Total Builds</p>
                  <p className="text-3xl font-bold text-gray-900">{overallSummary.totalBuilds}</p>
                </div>
                <BarChart3 className="h-8 w-8 text-blue-600" />
              </div>
            </div>

            <div className="card p-6 cursor-pointer hover:shadow-lg transition-shadow bg-gradient-to-r from-green-50 to-green-100" 
                 onClick={() => handleChartClick('overall', overallSummary)}>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Passed Builds</p>
                  <p className="text-3xl font-bold text-green-600">{overallSummary.totalBuildsPassed}</p>
                  <p className="text-sm text-gray-500">{overallSummary.percentPassed.toFixed(1)}%</p>
                </div>
                <TrendingUp className="h-8 w-8 text-green-600" />
              </div>
            </div>

            <div className="card p-6 cursor-pointer hover:shadow-lg transition-shadow bg-gradient-to-r from-red-50 to-red-100" 
                 onClick={() => handleChartClick('overall', overallSummary)}>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Failed Builds</p>
                  <p className="text-3xl font-bold text-red-600">{overallSummary.totalBuildsFailed}</p>
                  <p className="text-sm text-gray-500">{overallSummary.percentFailed.toFixed(1)}%</p>
                </div>
                <TrendingDown className="h-8 w-8 text-red-600" />
              </div>
            </div>

            <div className="card p-6 bg-gradient-to-r from-purple-50 to-purple-100">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Success Rate</p>
                  <p className="text-3xl font-bold text-purple-600">{overallSummary.percentPassed.toFixed(1)}%</p>
                </div>
                <div className={`h-8 w-8 rounded-full flex items-center justify-center ${
                  overallSummary.percentPassed >= 80 ? 'bg-green-100 text-green-600' : 
                  overallSummary.percentPassed >= 60 ? 'bg-yellow-100 text-yellow-600' : 
                  'bg-red-100 text-red-600'
                }`}>
                  ✓
                </div>
              </div>
            </div>
          </div>

          {/* Main Charts */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <PieChart 
              data={overallSummary} 
              title="Overall Success Rate" 
              color="#10b981" 
              onClick={() => handleChartClick('overall', overallSummary)}
            />
            <TrendChart 
              data={appSummaries.slice(0, 6)} 
              title="Application Performance Trends" 
            />
          </div>
        </>
      )}

      {/* Execution Distribution View */}
      {selectedView === 'execution-distribution' && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <PieChart 
            data={functionalSummary} 
            title="Functional Tests" 
            color="#10b981" 
            onClick={() => handleChartClick('execution-distribution', functionalSummary)}
          />
          <PieChart 
            data={regressionSummary} 
            title="Regression Tests" 
            color="#8b5cf6" 
            onClick={() => handleChartClick('execution-distribution', regressionSummary)}
          />
          <PieChart 
            data={sanitySummary} 
            title="Sanity Tests" 
            color="#f59e0b" 
            onClick={() => handleChartClick('execution-distribution', sanitySummary)}
          />
          <PieChart 
            data={smokeSummary} 
            title="Smoke Tests" 
            color="#6366f1" 
            onClick={() => handleChartClick('execution-distribution', smokeSummary)}
          />
        </div>
      )}

      {/* Applications View */}
      {selectedView === 'applications' && (
        <>
          <div className="card">
            <div className="px-6 py-4 border-b border-gray-200">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-semibold text-gray-900">Application Analysis</h2>
                <Filter className="h-5 w-5 text-gray-400" />
              </div>
            </div>
            <div className="p-6">
              <div className="mb-4">
                <label htmlFor="applicationSelect" className="block text-sm font-medium text-gray-700 mb-2">
                  Select Application:
                </label>
                <select
                  id="applicationSelect"
                  className="w-full md:w-1/2 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  value={selectedApp}
                  onChange={(e) => setSelectedApp(e.target.value)}
                >
                  <option value="">Choose an application...</option>
                  {applications.map((app) => (
                    <option key={app.applicationId} value={app.applicationId}>
                      {app.applicationName} ({app.applicationId})
                    </option>
                  ))}
                </select>
              </div>

              {appSummary && (
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-6">
                  <div className="text-center p-4 bg-gray-50 rounded-lg">
                    <div className="text-2xl font-bold text-gray-900">{appSummary.totalBuilds}</div>
                    <div className="text-sm text-gray-500">Total Builds</div>
                  </div>
                  <div className="text-center p-4 bg-green-50 rounded-lg">
                    <div className="text-2xl font-bold text-green-600">{appSummary.totalBuildsPassed}</div>
                    <div className="text-sm text-gray-500">Passed</div>
                  </div>
                  <div className="text-center p-4 bg-red-50 rounded-lg">
                    <div className="text-2xl font-bold text-red-600">{appSummary.totalBuildsFailed}</div>
                    <div className="text-sm text-gray-500">Failed</div>
                  </div>
                  <div className="text-center p-4 bg-blue-50 rounded-lg">
                    <div className="text-2xl font-bold text-blue-600">{appSummary.percentPassed.toFixed(1)}%</div>
                    <div className="text-sm text-gray-500">Success Rate</div>
                  </div>
                </div>
              )}
            </div>
          </div>

          <TrendChart 
            data={appSummaries} 
            title="All Applications Performance" 
          />
        </>
      )}

      {/* Trends View */}
      {selectedView === 'trends' && (
        <div className="space-y-6">
          {/* Date Range Filter for Trends */}
          <div className="card p-4">
            <h3 className="text-lg font-semibold text-gray-900 mb-3">Filter Trends by Date</h3>
            <select
              value={trendDateRange}
              onChange={(e) => setTrendDateRange(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="all">All Time</option>
              <option value="7days">Last 7 Days</option>
              <option value="30days">Last 30 Days</option>
            </select>
          </div>

          {/* Overall Trends */}
          <TrendChart
            data={filterTrendData(overallDailyTrends, trendDateRange)}
            title="Overall Execution Trends (Daily)"
          />

          {/* Application Specific Trends */}
          {selectedApp && applicationDailyTrends.length > 0 && (
            <TrendChart
              data={filterTrendData(applicationDailyTrends, trendDateRange)}
              title={`Execution Trends for ${applications.find(app => app.applicationId === selectedApp)?.applicationName || selectedApp} (Daily)`}
            />
          )}
          {!selectedApp && (
            <div className="card p-6 text-center text-gray-500">
              Select an application from the "Applications" tab to view its daily trends.
            </div>
          )}
        </div>
      )}

      {/* Applications Summary Table */}
      {selectedView === 'overview' && (
        <div className="card">
          <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
            <h2 className="text-xl font-semibold text-gray-900">Applications Summary</h2>
            <button
              onClick={() => handleChartClick('applications', null)}
              className="btn-primary flex items-center space-x-2"
            >
              <Eye size={18} />
              <span>View Details</span>
            </button>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Application
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Total Builds
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Passed
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Failed
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Success Rate
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {appSummaries.slice(0, 5).map((app) => (
                  <tr key={app.applicationId} className="hover:bg-gray-50 cursor-pointer"
                      onClick={() => handleChartClick('application', app)}>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{app.applicationName}</div>
                        <div className="text-sm text-gray-500">{app.applicationId}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {app.totalBuilds}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-green-600 font-medium">
                      {app.totalBuildsPassed}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-red-600 font-medium">
                      {app.totalBuildsFailed}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="flex-1 bg-gray-200 rounded-full h-2 mr-2">
                          <div
                            className="bg-green-600 h-2 rounded-full transition-all duration-1000"
                            style={{ width: `${app.percentPassed}%` }}
                          ></div>
                        </div>
                        <span className="text-sm text-gray-900">{app.percentPassed.toFixed(1)}%</span>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Data Table Modal */}
      {showTable && (
        <DataTable
          data={tableData}
          title={tableTitle}
          onClose={() => setShowTable(false)}
        />
      )}
    </div>
  )
}
